package org.cardanofoundation.ledgersync.scheduler.service.offchain;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.concurrent.ConcurrentHashMap;

public abstract class OffChainStoringService<E, F> {
    public abstract void insertFetchData(Collection<E> offChainAnchorData);

    public abstract void updateFetchData(Collection<E> offChainAnchorData);

    public abstract void insertFetchFailData(Collection<F> offChainFetchErrorData);

    protected static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
