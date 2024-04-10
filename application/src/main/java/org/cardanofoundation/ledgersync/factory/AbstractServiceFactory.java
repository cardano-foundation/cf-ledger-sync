package org.cardanofoundation.ledgersync.factory;

import jakarta.annotation.PostConstruct;
import org.cardanofoundation.ledgersync.service.SyncServiceInstance;

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
