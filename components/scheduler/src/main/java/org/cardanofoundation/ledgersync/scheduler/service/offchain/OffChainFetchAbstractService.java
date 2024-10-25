package org.cardanofoundation.ledgersync.scheduler.service.offchain;

import static com.bloxbean.cardano.client.util.JsonFieldWriter.mapper;

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
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.net.ssl.SSLException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.common.util.UrlUtil;
import org.cardanofoundation.ledgersync.scheduler.dto.anchor.AnchorDTO;
import org.cardanofoundation.ledgersync.scheduler.dto.offchain.OffChainFetchResultDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public abstract class OffChainFetchAbstractService<S, F, O extends OffChainFetchResultDTO, T extends AnchorDTO> {

    static final int TIMEOUT = 30000;
    static final int READ_TIMEOUT = 19000;
    static final int WRITE_TIMEOUT = 10000;
    static final int LIMIT_BYTES = 4096;

    protected Queue<O> offChainAnchorsFetchResult;

    public void initOffChainListData() {
        offChainAnchorsFetchResult = new ConcurrentLinkedQueue<>();
    }

    public abstract S extractOffChainData(O offChainAnchorData, Integer maxRetry);

    public abstract F extractFetchError(O offChainAnchorData);

    public abstract O addIdKey(OffChainFetchResultDTO offChainAnchorData, T anchor);

    final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    void setup() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<S> getOffChainAnchorsFetch(Integer maxRetry) {
        return offChainAnchorsFetchResult.stream()
            .map(e -> this.extractOffChainData(e, maxRetry))
            .toList();
    }

    public List<F> getOffChainAnchorsFetchError() {
        return offChainAnchorsFetchResult.stream()
            .filter(offChainFetchResult -> !offChainFetchResult.isValid())
            .map(this::extractFetchError)
            .toList();
    }

    public void crawlOffChainAnchors(Collection<T> anchors) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        Set<T> anchorSet = new HashSet<>(anchors);
        anchorSet.forEach(anchor -> futures.add(fetchAnchorUrl(anchor)));
        futures.forEach(CompletableFuture::join);
    }

    private CompletableFuture<Void> fetchAnchorUrl(T anchor) {
        try {
            if (!UrlUtil.isUrl(anchor.getAnchorUrl())) {
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
                    handleFetchFailure(throwable.getMessage(), anchor, null);
                    }
                )
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
        if (HttpStatus.OK.equals(statusCode) &&
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
        if (!HttpStatus.OK.equals(statusCode)) {
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
            try {
                JsonNode checkJsonContent = mapper.readTree(responseBody);

                // check CIPs later - save to fetch error if JSON not match CIPs
                handleFetchSuccess(anchor, responseBody);
            } catch (JsonProcessingException e) {
                handleFetchFailure(
                    "Error Anchor: JSON parser error from when fetching metadata from " + anchor.getAnchorUrl(), anchor,
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
            .build();
    }
}
