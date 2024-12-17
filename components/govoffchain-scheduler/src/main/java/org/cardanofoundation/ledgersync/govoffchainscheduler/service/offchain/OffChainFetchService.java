package org.cardanofoundation.ledgersync.govoffchainscheduler.service.offchain;

import static com.bloxbean.cardano.client.util.JsonFieldWriter.mapper;

import com.bloxbean.cardano.client.crypto.Blake2bUtil;
import com.github.jsonldjava.core.DocumentLoader;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.stream.Collectors;
import javax.net.ssl.SSLException;

import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.common.util.UrlUtil;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.anchor.AnchorDTO;
import org.cardanofoundation.ledgersync.govoffchainscheduler.dto.offchain.OffChainFetchResultDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public abstract class OffChainFetchService<S, F, O extends OffChainFetchResultDTO, T extends AnchorDTO> {

    static final int TIMEOUT = 30000;
    static final int READ_TIMEOUT = 19000;
    static final int WRITE_TIMEOUT = 10000;
    static final int LIMIT_BYTES = 4096;

    protected Queue<O> offChainAnchorsFetchResult;

    public void initOffChainListData() {
        offChainAnchorsFetchResult = new ConcurrentLinkedQueue<>();
    }

    public abstract S extractOffChainData(O offChainAnchorData);

    public abstract F extractFetchError(O offChainAnchorData);

    public abstract O addIdKey(OffChainFetchResultDTO offChainAnchorData, T anchor);

    final ObjectMapper objectMapper = new ObjectMapper();

    protected final ExecutorService executor;

    protected OffChainFetchService(ExecutorService executor) {
        this.executor = executor;
    }

    @PostConstruct
    void setup() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<S> getOffChainAnchorsFetch() {
        return offChainAnchorsFetchResult.stream()
            .map(this::extractOffChainData)
            .collect(Collectors.toList());
    }

    public List<F> getOffChainAnchorsFetchError() {
        return offChainAnchorsFetchResult.stream()
            .filter(offChainFetchResult -> !offChainFetchResult.isValid())
            .map(this::extractFetchError)
            .collect(Collectors.toList());
    }

    public void crawlOffChainAnchors(Collection<T> anchors) {
        try {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            Set<T> anchorSet = new HashSet<>(anchors);

            anchorSet.forEach(anchor -> futures
                .add(CompletableFuture.supplyAsync(() -> fetchAnchorUrl(anchor), executor)
                    .thenCompose(e -> e)));

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            log.error("Error when run thread pool in crawl url - ", e.getCause());
        }
    }

    private CompletableFuture<Void> fetchAnchorUrl(T anchor) {
        try {
            if (!UrlUtil.isUrlValid(anchor.getAnchorUrl())) {
                handleFetchFailure("Invalid URL", anchor, null);
                return CompletableFuture.completedFuture(null);
            }
            return buildWebClient()
                .get()
                .uri(UrlUtil.formatSpecialCharactersUrl(anchor.getAnchorUrl()))
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .toEntity(String.class)
                .timeout(Duration.ofMillis(TIMEOUT))
                .doOnError(Exception.class, throwable -> {
                    assert throwable != null;
                    handleFetchFailure(
                        throwable.getCause() == null ? throwable.getMessage() : throwable.getCause().getMessage()
                        , anchor
                        , null);
                })
                .toFuture()
                .thenAccept(responseEntity -> handleResponse(responseEntity, anchor))
                .exceptionally(
                    throwable -> {
                        log.info("Error when fetch data from URL: {}", throwable.getMessage());
                        handleFetchFailure(throwable.getMessage(), anchor, null);
                        return null;
                    });

        } catch (Exception e) {
            handleFetchFailure(e.getMessage(), anchor, null);
            return CompletableFuture.completedFuture(null);
        }
    }

    private void handleResponse(ResponseEntity<String> response, T anchor) {
        HttpStatusCode statusCode = response.getStatusCode();

        // if content type is not supported, then send to fail queue
        if (statusCode.is2xxSuccessful() &&
            Objects.requireNonNull(response.getHeaders().get(HttpHeaders.CONTENT_TYPE)).stream()
            .noneMatch(
                contentType ->
                    contentType.contains(MediaType.APPLICATION_JSON_VALUE)
                        || contentType.contains("application/ld+json")
                        || contentType.contains(MediaType.TEXT_PLAIN_VALUE)
                        || contentType.contains("text/json"))) {
            handleFetchFailure(
                "Content type not supported " + response.getHeaders().get(HttpHeaders.CONTENT_TYPE),
                anchor, null);
            return;
        }

        // if status code is not OK, then send to fail queue
        if (!statusCode.is2xxSuccessful()) {
            handleFetchFailure(statusCode.toString(), anchor, null);
            return;
        }

        // if content length is greater than limit, then send to fail queue
        if (Objects.requireNonNull(response.getBody()).getBytes().length > LIMIT_BYTES) {
            handleFetchFailure("Content length exceed limit", anchor, null);
            return;
        }

        var responseBody = response.getBody();
        if (Objects.nonNull(responseBody)) {
            // check anchor url and anchor hash is match
            if (!isUrlAndHashValid(responseBody.getBytes(StandardCharsets.UTF_8), anchor)) {
                return;
            }

            try {
                // check json format
                JsonNode checkJsonContent = mapper.readTree(responseBody);

                if (!isValidJsonLd(responseBody)){
                    handleFetchFailure(
                        "Response content is not in JSON-LD format or IRI not valid.",
                        anchor,
                        responseBody);
                    return;
                }

                // check CIPs later - save to fetch error if JSON not match CIPs
                handleFetchSuccess(anchor, responseBody);
            } catch (JsonProcessingException e) {
                handleFetchFailure(
                    "Error Anchor: JSON parser error from when fetching metadata from " + anchor.getAnchorUrl(),
                    anchor,
                    null);
                log.info("Error when parse data to object from URL: {}", e.getMessage());
            }
        } else {
            handleFetchFailure(
                "Error Anchor: response body is null when fetching metadata from " + anchor.getAnchorUrl(), anchor,
                null);
        }
    }

    private void handleFetchFailure(String error, T anchor, String content) {
        OffChainFetchResultDTO data =
            OffChainFetchResultDTO.builder()
                .anchorHash(anchor.getAnchorHash())
                .anchorUrl(anchor.getAnchorUrl())
                .isValid(false)
                .rawData(content)
                .fetchFailError(error == null ? "Unknown error" : error)
                .slotNo(anchor.getSlot())
                .retryCount(anchor.getRetryCount())
                .build();

        O ocFetchResult = addIdKey(data, anchor);
        offChainAnchorsFetchResult.add(ocFetchResult);
    }

    private void handleFetchSuccess(T anchor, String responseBody) {
        OffChainFetchResultDTO data =
            OffChainFetchResultDTO.builder()
                .anchorHash(anchor.getAnchorHash())
                .anchorUrl(anchor.getAnchorUrl())
                .isValid(true)
                .fetchFailError(null)
                .rawData(responseBody)
                .slotNo(anchor.getSlot())
                .retryCount(anchor.getRetryCount())
                .build();

        O ocFetchResult = addIdKey(data, anchor);
        offChainAnchorsFetchResult.add(ocFetchResult);
    }

    private boolean isUrlAndHashValid(byte[] responseToBytes, T anchor) {
        byte[] shaInBytes = Blake2bUtil.blake2bHash256(responseToBytes);
        String hashFromResponse = HexUtil.bytesToHex(shaInBytes);

        if (!hashFromResponse.equals(anchor.getAnchorHash())) {
            handleFetchFailure(
                "Hash mismatch when fetching metadata from "
                    + anchor.getAnchorUrl()
                    + ". Expected \"" + anchor.getAnchorHash()
                    + "\" but got \"" + hashFromResponse + "\".",
                anchor,
                null);
            return false;
        }
        return true;
    }

    private static WebClient buildWebClient() throws SSLException {
        var sslContext =
            SslContextBuilder.forClient()
                .sslProvider(SslProvider.JDK)
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .startTls(true)
                .build();

        var httpClient =
            HttpClient.create()
                .wiretap(Boolean.FALSE)
                .secure(t -> t.sslContext(sslContext))
                .followRedirect(Boolean.TRUE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .responseTimeout(Duration.ofMillis(TIMEOUT))
                .doOnConnected(
                    connection -> {
                        connection.addHandlerFirst(
                            new ReadTimeoutHandler(READ_TIMEOUT, TimeUnit.MILLISECONDS));
                        connection.addHandlerFirst(
                            new WriteTimeoutHandler(WRITE_TIMEOUT, TimeUnit.MILLISECONDS));
                    });

        return WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .codecs(configure -> configure.defaultCodecs().maxInMemorySize(512 * 1024))
            .build();
    }

    public static boolean isValidJsonLd(String jsonContent) {
        try {
            Object jsonObject = JsonUtils.fromString(jsonContent);

            JsonLdOptions options = new JsonLdOptions();
            options.setDocumentLoader(new DocumentLoader());

            Map<String, Object> compacted = JsonLdProcessor.compact(jsonObject, null, options);

            return !compacted.isEmpty();
        } catch (IOException | JsonLdError e) {
            return false;
        }
    }
}
