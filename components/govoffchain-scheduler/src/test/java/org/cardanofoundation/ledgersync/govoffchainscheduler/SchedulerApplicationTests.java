package org.cardanofoundation.ledgersync.govoffchainscheduler;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.*;

@SpringBootTest(classes = WebClientAutoConfiguration.class)
class SchedulerApplicationTests {

    @Autowired
    WebClient.Builder webClientBuilder;

    @Test
    void contextLoads() throws SSLException {
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1");
        var sslContext =
                SslContextBuilder.forClient()
                        .sslProvider(SslProvider.JDK)
                        .startTls(true)
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();

        var httpClient =
                HttpClient.create()
                        .wiretap(
                                "reactor.netty.http.client.HttpClient",
                                LogLevel.DEBUG,
                                AdvancedByteBufFormat.TEXTUAL,
                                StandardCharsets.UTF_8)
                        // .wiretap(Boolean.FALSE)
                        // .secure(t -> t.sslContext(sslContext))
                        .followRedirect(Boolean.TRUE)
                        .responseTimeout(Duration.ofSeconds(30000))
                        .protocol(HttpProtocol.HTTP11)
                        .doOnConnected(
                                connection -> {
                                    connection.addHandlerFirst(new ReadTimeoutHandler(19000, TimeUnit.SECONDS));
                                    connection.addHandlerFirst(new WriteTimeoutHandler(10000, TimeUnit.SECONDS));
                                });

        webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build()
                .get()
                .uri("gist.github.com/pavanvora/42f63b97e3311fb344f8d0a1595fd4d4")
                .retrieve()
                .toEntity(String.class)
                .map(
                        response -> {
                            HttpStatusCode statusCode = response.getStatusCode();
                            if (statusCode.equals(NOT_FOUND)) {
                                return Optional.empty();
                            } else if (statusCode.equals(
                                    FORBIDDEN)) { // log.error("FORBIDDEN for url: {}", poolHash.getUrl());
                                return Optional.empty();
                            } else if (statusCode.equals(
                                    REQUEST_TIMEOUT)) { // log.error("REQUEST_TIMEOUT for url: {}",
                                // poolHash.getUrl());
                                return Optional.empty();
                            } else if (statusCode.equals(
                                    MOVED_PERMANENTLY)) { // log.error("MOVED PERMANENTLY for url: {}",
                                // poolHash.getUrl());
                                return Optional.empty();
                            } else if (statusCode.equals(OK)) {
                                if (response.getHeaders().get(HttpHeaders.CONTENT_TYPE).stream()
                                        .noneMatch(
                                                contentType ->
                                                        contentType.contains(MediaType.APPLICATION_JSON_VALUE)
                                                                || contentType.contains(MediaType.TEXT_PLAIN_VALUE)
                                                                || contentType.contains(
                                                                MediaType.APPLICATION_OCTET_STREAM_VALUE))) {
                                    return Optional.empty();
                                }

                                if (response.getBody().getBytes().length > 512) {
                                    return Optional.empty();
                                }
                                return Optional.of(response);
                            }
                            return Optional.of(response);
                        })
                .block()
                .ifPresent(System.out::println);
    }

    @Test
    void httpUrlClient() throws IOException {
        URL url = new URL(("https://cardanostakehouse.com/1404d233-c0f4-47fb-bdcb-321.json"));
        HttpURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod(RequestMethod.GET.name());
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        StringBuilder builder = new StringBuilder();
        InputStreamReader streamReader =
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8);

        int output;
        while ((output = streamReader.read()) != -1) {
            builder.append((char) output);
        }

        streamReader.close();

        System.out.println(builder.toString());
    }

    @Test
    void httpClient() throws URISyntaxException {
        java.net.http.HttpClient client =
                java.net.http.HttpClient.newBuilder()
                        .version(java.net.http.HttpClient.Version.HTTP_1_1)
                        .followRedirects(java.net.http.HttpClient.Redirect.NORMAL)
                        .build();

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(new URI("https://cardanostakehouse.com/1404d233-c0f4-47fb-bdcb-321.json"))
                        .GET()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .timeout(Duration.ofSeconds(10))
                        .build();

        client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
    }

}
