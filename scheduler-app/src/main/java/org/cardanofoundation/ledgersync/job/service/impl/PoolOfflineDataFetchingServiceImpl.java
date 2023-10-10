package org.cardanofoundation.ledgersync.job.service.impl;

import com.bloxbean.cardano.client.crypto.Blake2bUtil;
import com.bloxbean.cardano.client.util.HexUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.cardanofoundation.ledgersync.common.util.UrlUtil;
import org.cardanofoundation.ledgersync.job.constant.JobConstants;
import org.cardanofoundation.ledgersync.job.projection.PoolHashUrlProjection;
import org.cardanofoundation.ledgersync.job.repository.PoolHashRepository;
import org.cardanofoundation.ledgersync.job.service.PoolOfflineDataFetchingService;
import org.cardanofoundation.ledgersync.schedulecommon.dto.PoolData;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.http.HttpStatus.OK;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class PoolOfflineDataFetchingServiceImpl implements PoolOfflineDataFetchingService {

  public static final String EXTENDED = "extended";
  public static final String URL_PNG_LOGO = "url_png_logo";
  public static final String URL_PNG_ICON_64_X_64 = "url_png_icon_64x64";
  public static final int URL_LIMIT = 2000;
  static final int TIMEOUT = 30000;
  static final int READ_TIMEOUT = 19000;
  static final int WRITE_TIMEOUT = 10000;
  static final int LIMIT_BYTES = 2048;
  final PoolHashRepository poolHashRepository;
  final WebClient.Builder webClientBuilder;
  final ObjectMapper objectMapper;
  List<PoolData> poolDataList;

  @Override
  public List<PoolData> fetchPoolOfflineData() {
    long startTime = System.currentTimeMillis();
    log.info("Start fetch pool offline data");
    poolDataList = new ArrayList<>();
    List<CompletableFuture<Void>> futures = new ArrayList<>();
    int start = 0;
    while (true) {
      List<PoolHashUrlProjection> poolHashUrlProjections = poolHashRepository.findPoolHashAndUrl(
          PageRequest.of(start, JobConstants.DEFAULT_BATCH));

      if (CollectionUtils.isEmpty(poolHashUrlProjections)) {
        break;
      }
      poolHashUrlProjections.forEach(poolHash -> futures.add(fetchPoolOffLineMetaData(poolHash)));
      start = start + 1;
    }

    futures.forEach(CompletableFuture::join);
    log.info("Fetched pool offline data count: {}, time taken: {} ms", poolDataList.size(),
             System.currentTimeMillis() - startTime);
    return poolDataList;
  }

  @Override
  public void fetchPoolOfflineDataLogo(List<PoolData> poolDataSuccess) {
    long startTime = System.currentTimeMillis();
    log.info("Start fetch pool offline logo, success pool count: {}", poolDataSuccess.size());
    List<CompletableFuture<Void>> futures = new ArrayList<>();
    poolDataSuccess.forEach(poolData -> futures.add(fetchPoolOfflineDataLogo(poolData)));
    futures.forEach(CompletableFuture::join);

    long fetchedCount = poolDataSuccess.stream()
        .filter(poolData -> !ObjectUtils.isEmpty(poolData.getLogoUrl()) ||
            !ObjectUtils.isEmpty(poolData.getIconUrl()))
        .count();
    log.info("Fetched pool offline logo count: {}, time taken: {} ms", fetchedCount,
             System.currentTimeMillis() - startTime);
  }


  /**
   * Asynchronously fetching metadata from a specified URL(poolHash.url)
   * using a WebClient in a non-blocking manner.
   *
   * @param poolHash
   * @return a CompletableFuture<Void> representing the asynchronous operation of
   * fetching the metadata from the URL.
   */
  private CompletableFuture<Void> fetchPoolOffLineMetaData(PoolHashUrlProjection poolHash) {
    try {
      if (!UrlUtil.isUrl(poolHash.getUrl())) {
        fetchFail("not valid url may contain special character", poolHash);
        return CompletableFuture.completedFuture(null);
      }
      return buildWebClient()
          .get()
          .uri(UrlUtil.formatSpecialCharactersUrl(poolHash.getUrl()))
          .acceptCharset(StandardCharsets.UTF_8)
          .retrieve()
          .toEntity(String.class)
          .timeout(Duration.ofMillis(TIMEOUT))
          .doOnError(Exception.class, throwable -> fetchFail(throwable.getMessage(), poolHash))
          .toFuture()
          .thenAccept(responseEntity -> handleResponse(responseEntity, poolHash))
          .exceptionally(throwable -> {
            fetchFail(throwable.getMessage(), poolHash);
            return null;
          });
    } catch (Exception e) {
      fetchFail(e.getMessage(), poolHash);
      return CompletableFuture.completedFuture(null);
    }
  }

  /**
   * Checks the status code, content type, and content length of the response.
   * It calls the appropriate methods (fetchFail or fetchSuccess)
   * based on the analysis of the response, indicating success or failure.
   * @param response the response from the WebClient
   * @param poolHash the poolHash object
   */
  private void handleResponse(ResponseEntity<String> response, PoolHashUrlProjection poolHash) {
    HttpStatusCode statusCode = response.getStatusCode();

    // if status code is not OK, then send to fail queue
    if (!HttpStatus.OK.equals(statusCode)) {
      fetchFail(statusCode.toString(), poolHash);
      return;
    }

    // if content type is not supported, then send to fail queue
    if (Objects.requireNonNull(response.getHeaders().get(HttpHeaders.CONTENT_TYPE))
        .stream()
        .noneMatch(contentType -> contentType.contains(MediaType.APPLICATION_JSON_VALUE)
            || contentType.contains(MediaType.TEXT_PLAIN_VALUE)
            || contentType.contains("binary/octet-stream")
            || contentType.contains("text/json")
            || contentType.contains("application/binary")
            || contentType.contains(MediaType.APPLICATION_OCTET_STREAM_VALUE))) {
      fetchFail("Content type not supported " + response.getHeaders().get(HttpHeaders.CONTENT_TYPE),
                poolHash);
      return;
    }

    // if content length is greater than limit, then send to fail queue
    if (Objects.requireNonNull(response.getBody()).getBytes().length > LIMIT_BYTES) {
      fetchFail("Content length exceed limit", poolHash);
      return;
    }

    var responseBody = response.getBody();
    if (Objects.nonNull(responseBody)) {
      fetchSuccess(poolHash, responseBody);
    }
  }


  /**
   * Fetches offline data and logo for a given extended url asynchronously using a WebClient.
   * It checks for the presence of extended data, fetches it, processes it,
   * and handles any exceptions that may occur during the execution.
   * @param poolData the poolData object
   * @return a CompletableFuture<Void> representing the asynchronous operation of
   * fetching pool offline data and logo from the URL.
   */
  private CompletableFuture<Void> fetchPoolOfflineDataLogo(PoolData poolData) {
    try {
      final Map<String, Object> map = objectMapper.readValue(new String(poolData.getJson()),
                                                             new TypeReference<>() {});
      // if pool metadata have no extended field then skip
      if (map.containsKey(EXTENDED)) {
        String poolExtendedUrl = String.valueOf(map.get(EXTENDED));

        if (!UrlUtil.isUrl(poolExtendedUrl)) {
          return CompletableFuture.completedFuture(null);
        }

        return buildWebClient()
            .get()
            .uri(UrlUtil.formatSpecialCharactersUrl(poolExtendedUrl))
            .acceptCharset(StandardCharsets.UTF_8)
            .retrieve()
            .toEntity(String.class)
            .timeout(Duration.ofMillis(TIMEOUT))
            .doOnError(Exception.class,
                       throwable -> fetchFail(throwable.getMessage(), poolData))
            .toFuture()
            .thenAccept(responsePoolExtended -> {
              try {
                String extendBody = responsePoolExtended.getBody();
                // make another try catch for catching extendMap only and not affect pool
                final Map<String, Object> extendMap = objectMapper.readValue(extendBody,
                                                                             new TypeReference<>() {});
                findExtendedLogoWithLoop(extendMap, poolData);
              } catch (JsonProcessingException ignored) {
              }
            })
            .exceptionally(throwable -> null);
      }
    } catch (Exception ignored) {
    }
    return CompletableFuture.completedFuture(null);
  }

  /**
   * Recursively searches for specific keys in a map and extracts corresponding values as extended logos,
   * updating the PoolData object with the URLs if they meet certain conditions.
   *
   * @param map the map to be searched
   * @param poolData the poolData object
   */
  private void findExtendedLogoWithLoop(Map<String, Object> map, PoolData poolData) {
    map.keySet()
        .forEach(
            key -> {
              if (map.get(key) instanceof LinkedHashMap<?, ?> subMap) {
                findExtendedLogoWithLoop((LinkedHashMap<String, Object>) subMap, poolData);
                return;
              }

              if (key.equals(URL_PNG_LOGO)) {
                AtomicReference<String> urlLogo = new AtomicReference<>("");
                Optional.ofNullable(map.get(URL_PNG_LOGO))
                    .ifPresent(url -> urlLogo.set(String.valueOf(url)));
                if (urlLogo.get().length() < URL_LIMIT && UrlUtil.isUrl(urlLogo.get())) {
                  poolData.setLogoUrl(urlLogo.get());
                }
              }

              if (key.equals(URL_PNG_ICON_64_X_64)) {
                AtomicReference<String> urlIcon = new AtomicReference<>("");
                Optional.ofNullable(map.get(URL_PNG_ICON_64_X_64))
                    .ifPresent(url -> urlIcon.set(String.valueOf(url)));

                if (urlIcon.get().length() < URL_LIMIT && UrlUtil.isUrl(urlIcon.get())) {
                  poolData.setIconUrl(urlIcon.get());
                }
              }
            });
  }

  /**
   * Constructs and configures a WebClient with specific settings for handling HTTP requests,
   * including SSL configuration, timeout settings, and headers.
   *
   * @return webclient
   * @throws SSLException
   */
  private WebClient buildWebClient() throws SSLException {

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

    return webClientBuilder
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();
  }

  /**
   * Processes a successful fetch operation by creating a PoolData object with relevant information
   * from the response and adding it to a list for further use.
   *
   * @param poolHash
   * @param responseBody
   */
  private void fetchSuccess(PoolHashUrlProjection poolHash, String responseBody) {
    PoolData data = PoolData.builder()
        .valid(Boolean.TRUE)
        .status(OK.value())
        .json(responseBody.getBytes(StandardCharsets.UTF_8))
        .poolId(poolHash.getPoolId())
        .metadataRefId(poolHash.getMetadataId())
        .hash(HexUtil.encodeHexString(Blake2bUtil.blake2bHash256(responseBody.getBytes())))
        .build();

    poolDataList.add(data);
  }

  /**
   * Handles a failed fetch operation by creating a PoolHashUrlProjection object with the appropriate failure
   * information and adding it to a list for further use.
   *
   * @param error the error message
   * @param poolHash the PoolHashUrlProjection object
   */
  private void fetchFail(String error, PoolHashUrlProjection poolHash) {
    PoolData data =
        PoolData.builder()
            .valid(Boolean.FALSE)
            .errorMessage(String.format("%s %s", error, poolHash.getUrl()))
            .poolId(poolHash.getPoolId())
            .metadataRefId(poolHash.getMetadataId())
            .build();

    poolDataList.add(data);
  }

  /**
   * Handles a failed fetch operation by creating a PoolHashUrlProjection object with the appropriate failure
   * information and adding it to a list for further use.
   *
   * @param error the error message
   * @param poolHash the poolHash object
   */
  private void fetchFail(String error, PoolData poolHash) {
    PoolData data =
        PoolData.builder()
            .valid(Boolean.FALSE)
            .errorMessage(
                String.format("%s %s %s", error, poolHash.getPoolId(), poolHash.getMetadataRefId()))
            .poolId(poolHash.getPoolId())
            .metadataRefId(poolHash.getMetadataRefId())
            .build();

    poolDataList.add(data);
  }
}
