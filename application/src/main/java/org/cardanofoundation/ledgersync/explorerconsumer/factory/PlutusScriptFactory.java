package org.cardanofoundation.ledgersync.explorerconsumer.factory;

import com.bloxbean.cardano.client.plutus.spec.PlutusScript;
import jakarta.annotation.PostConstruct;
import org.cardanofoundation.explorer.consumercommon.entity.Script;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.plutus.PlutusScriptService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PlutusScriptFactory extends
        AbstractServiceFactory<PlutusScriptService<? extends PlutusScript>, PlutusScriptService> {// NOSONAR

    protected PlutusScriptFactory(
            List<PlutusScriptService<? extends PlutusScript>> services) {
        super(services);
    }

    @Override
    @PostConstruct
    void init() {
        serviceMap = services.stream().collect(Collectors.toMap(PlutusScriptService::supports,
                Function.identity())
        );
    }

    @SuppressWarnings("unchecked")
    public Script handle(PlutusScript plutusScript, Tx tx) {
        return serviceMap.get(plutusScript.getClass()).handle(plutusScript, tx);
    }
}
