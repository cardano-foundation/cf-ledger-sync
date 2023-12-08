package org.cardanofoundation.ledgersync.aggregate.account.service;

import java.lang.reflect.ParameterizedType;

public interface SyncServiceInstance<T> {

    @SuppressWarnings("unchecked")
    default Class<?> supports() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }
}
