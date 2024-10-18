package org.cardanofoundation.ledgersync.scheduler.service.impl;

import static org.springframework.http.HttpStatus.OK;

import com.bloxbean.cardano.client.crypto.Blake2bUtil;
import com.bloxbean.cardano.client.util.HexUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.net.ssl.SSLException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.common.common.nativescript.RequireTimeAfter;
import org.cardanofoundation.ledgersync.common.common.nativescript.RequireTimeBefore;
import org.cardanofoundation.ledgersync.common.common.nativescript.ScriptAll;
import org.cardanofoundation.ledgersync.common.common.nativescript.ScriptAny;
import org.cardanofoundation.ledgersync.common.common.nativescript.ScriptAtLeast;
import org.cardanofoundation.ledgersync.common.common.nativescript.ScriptPubkey;
import org.cardanofoundation.ledgersync.common.common.nativescript.ScriptType;
import org.cardanofoundation.ledgersync.common.exception.ScriptException;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainDataCheckpoint;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainFetchError;
import org.cardanofoundation.ledgersync.consumercommon.entity.OffChainGovAction;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.OffChainCheckpointType;
import org.cardanofoundation.ledgersync.consumercommon.enumeration.TypeVote;
import org.cardanofoundation.ledgersync.scheduler.dto.PoolData;
import org.cardanofoundation.ledgersync.scheduler.projection.PoolHashUrlProjection;
import org.cardanofoundation.ledgersync.scheduler.service.OffChainRetryDataErrorService;
import org.cardanofoundation.ledgersync.scheduler.storage.PoolHashStorage;
import org.cardanofoundation.ledgersync.scheduler.storage.offchain.OffChainFetchErrorStorage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class OffChainRetryDataErrorServiceImpl implements OffChainRetryDataErrorService {

    static final Integer MAX_RETRY = 10;
    final ObjectMapper objectMapper;
    final OffChainFetchErrorStorage offChainFetchErrorStorage;
    final OffChainServiceImpl offChainServiceImpl;

    @Override
    public void retryOffChainErrorData() {
        long startTime = System.currentTimeMillis();
        log.info("Start retry error offchain data");
        retryOffChainGovErrorData(startTime);

        log.info("Retry error offchain data time taken: {} ms", System.currentTimeMillis() - startTime);
    }

    private void retryOffChainGovErrorData(long startTime) {
        OffChainDataCheckpoint currentCheckpoint;

        Map<TypeVote, List<OffChainFetchError>> listFetchErrors =
            offChainFetchErrorStorage.findByRetryCountLessThanEqualAndValidAfterRetryFalse(MAX_RETRY)
                .stream()
                .collect(Collectors.groupingBy(OffChainFetchError::getTypeVote, Collectors.toCollection(ArrayList::new)));

        // Todo
    }

}
