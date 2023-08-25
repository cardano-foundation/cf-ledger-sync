package org.cardanofoundation.ledgersync.explorerconsumer.factory;

import jakarta.annotation.PostConstruct;
import org.cardanofoundation.ledgersync.explorerconsumer.service.SyncServiceInstance;

import java.util.List;
import java.util.Map;

public abstract class AbstractServiceFactory<T extends SyncServiceInstance, I> {
    protected final List<T> services;
    protected Map<Class<?>, I> serviceMap;

    protected AbstractServiceFactory(List<T> services) {
        this.services = services;
    }

    @PostConstruct
    abstract void init();
}
