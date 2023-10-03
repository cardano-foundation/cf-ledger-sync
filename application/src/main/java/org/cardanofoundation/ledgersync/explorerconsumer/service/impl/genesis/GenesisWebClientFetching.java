package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.genesis;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.explorerconsumer.service.GenesisFetching;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigInteger;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Profile("internet")
public class GenesisWebClientFetching implements GenesisFetching {

    final WebClient.Builder webClient;

    /**
     * get json string from input url
     *
     * @param url
     * @return json String
     */
    @Override
    public String getContent(String url) {
        return webClient.baseUrl(url).build()
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMinutes(BigInteger.ONE.longValue()))
                .retry(BigInteger.TEN.intValue())
                .doOnError(err -> log.error(err.getMessage()))
                .block();
    }
}


