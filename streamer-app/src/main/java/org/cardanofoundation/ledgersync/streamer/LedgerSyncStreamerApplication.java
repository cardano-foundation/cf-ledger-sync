package org.cardanofoundation.ledgersync.streamer;

import com.bloxbean.cardano.yaci.core.config.YaciConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.cardanofoundation.*")
@Slf4j
public class LedgerSyncStreamerApplication   {

    //TODO -- Move these properties to Yaci Store
    @Value("${store.include-block-cbor:false}")
    private boolean includeBlockCbor;

    @Value("${store.include-txbody-cbor:false}")
    private boolean includeTxCbor;

    public static void main(String[] args) {
        SpringApplication.run(LedgerSyncStreamerApplication.class, args);
    }

    @PostConstruct
    public void init() {
        if (includeBlockCbor)
            YaciConfig.INSTANCE.setReturnBlockCbor(true);

        if (includeTxCbor)
            YaciConfig.INSTANCE.setReturnTxBodyCbor(true);
    }
}
