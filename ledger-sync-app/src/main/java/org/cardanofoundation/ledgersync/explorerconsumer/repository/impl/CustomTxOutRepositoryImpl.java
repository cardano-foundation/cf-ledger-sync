package org.cardanofoundation.ledgersync.explorerconsumer.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.explorer.consumercommon.entity.*;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.TxOutProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.CustomTxOutRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomTxOutRepositoryImpl implements CustomTxOutRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<TxOutProjection> findTxOutsByTxHashInAndTxIndexIn(
            List<Pair<String, Short>> txHashIndexPairs) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var txOutQuery = criteriaBuilder.createQuery(TxOutProjection.class);
        Root<TxOut> txOutRoot = txOutQuery.from(TxOut.class);
        Join<TxOut, Tx> tx = txOutRoot.join(TxOut_.tx);
        Join<TxOut, StakeAddress> stakeAddress = txOutRoot.join(TxOut_.stakeAddress, JoinType.LEFT);
        var txOutSelection = buildTxOutSelectQuery(criteriaBuilder, txOutRoot, tx, stakeAddress);

        Predicate[] predicates = txHashIndexPairs.stream()
                .map(txHashIndexPair ->
                        buildTxOutPredicate(criteriaBuilder, txOutRoot, tx, txHashIndexPair))
                .toArray(Predicate[]::new);

        txOutQuery.select(txOutSelection).where(criteriaBuilder.or(predicates));
        return entityManager.createQuery(txOutQuery).getResultList();
    }

    private static Predicate buildTxOutPredicate(
            CriteriaBuilder criteriaBuilder, Root<TxOut> txOutRoot, Join<TxOut, Tx> tx,
            Pair<String, Short> txHashIndexPair) {
        String txHash = txHashIndexPair.getFirst();
        int index = txHashIndexPair.getSecond();
        Predicate txHashEquals = criteriaBuilder.equal(tx.get(Tx_.hash), txHash);
        Predicate indexEquals = criteriaBuilder.equal(txOutRoot.get(TxOut_.index), index);

        return criteriaBuilder.and(txHashEquals, indexEquals);
    }

    private static CompoundSelection<TxOutProjection> buildTxOutSelectQuery(
            CriteriaBuilder criteriaBuilder, Root<TxOut> txOutRoot,
            Join<TxOut, Tx> tx, Join<TxOut, StakeAddress> stakeAddress) {
        return criteriaBuilder.construct(
                TxOutProjection.class,
                txOutRoot.get(BaseEntity_.id),
                tx.get(Tx_.hash),
                tx.get(BaseEntity_.id),
                txOutRoot.get(TxOut_.index),
                txOutRoot.get(TxOut_.value),
                stakeAddress.get(BaseEntity_.id),
                txOutRoot.get(TxOut_.address),
                txOutRoot.get(TxOut_.addressHasScript),
                txOutRoot.get(TxOut_.paymentCred)
        );
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Optional<TxOutProjection> findTxOutByTxHashAndTxOutIndex(String txHash, Short index) {
        return findTxOutsByTxHashInAndTxIndexIn(List.of(Pair.of(txHash, index)))
                .stream()
                .findFirst();
    }
}
