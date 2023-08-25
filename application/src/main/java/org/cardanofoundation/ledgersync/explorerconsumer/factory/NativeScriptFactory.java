package org.cardanofoundation.ledgersync.explorerconsumer.factory;

import jakarta.annotation.PostConstruct;
import org.cardanofoundation.explorer.consumercommon.entity.Script;
import org.cardanofoundation.explorer.consumercommon.entity.Tx;
import org.cardanofoundation.ledgersync.common.common.nativescript.NativeScript;
import org.cardanofoundation.ledgersync.explorerconsumer.service.impl.nativescript.NativeScriptService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NativeScriptFactory extends
        AbstractServiceFactory<NativeScriptService<? extends NativeScript>, NativeScriptService> {// NOSONAR

    protected NativeScriptFactory(
            List<NativeScriptService<? extends NativeScript>> services) {
        super(services);
    }

    @Override
    @PostConstruct
    void init() {
        serviceMap = services.stream().collect(Collectors.toMap(NativeScriptService::supports,
                Function.identity())
        );
    }

    @SuppressWarnings("unchecked")
    public Script handle(NativeScript nativeScript, Tx tx) {
        return serviceMap.get(nativeScript.getClass()).handle(nativeScript, tx);
    }
}
