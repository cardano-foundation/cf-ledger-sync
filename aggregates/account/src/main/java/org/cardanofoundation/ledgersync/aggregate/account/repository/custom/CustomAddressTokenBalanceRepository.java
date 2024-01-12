package org.cardanofoundation.ledgersync.aggregate.account.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.ledgersync.aggregate.account.repository.model.Address;
import org.cardanofoundation.ledgersync.aggregate.account.repository.model.AddressTokenBalance;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomAddressTokenBalanceRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Collection<AddressTokenBalance> findAllByAddressFingerprintPairIn(
            Collection<Pair<String, String>> addressFingerprintPairs) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(AddressTokenBalance.class);
        Root<AddressTokenBalance> addressTokenBalanceRoot = query.from(AddressTokenBalance.class);
        Join<AddressTokenBalance, Address> addressJoin =
                addressTokenBalanceRoot.join("address");
        Predicate[] predicates = addressFingerprintPairs.stream()
                .map(addressFingerprintPair ->
                        buildAddressTokenBalancePredicate(
                                criteriaBuilder, addressJoin, addressTokenBalanceRoot, addressFingerprintPair))
                .toArray(Predicate[]::new);

        query.select(addressTokenBalanceRoot).where(criteriaBuilder.or(predicates));
        return entityManager.createQuery(query).getResultList();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Collection<AddressTokenBalance> findAllByAddressMultiAssetIdPairIn(
            Collection<Pair<Long, String>> addressMultiAssetPairs) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(AddressTokenBalance.class);
        Root<AddressTokenBalance> addressTokenBalanceRoot = query.from(AddressTokenBalance.class);
        Join<AddressTokenBalance, Address> addressJoin =
                addressTokenBalanceRoot.join("address");

        Predicate[] predicates = addressMultiAssetPairs.stream()
                .map(addressMultiAssetPair ->
                        buildAddressTokenBalancePredicateWithIdPair(
                                criteriaBuilder, addressJoin, addressTokenBalanceRoot, addressMultiAssetPair))
                .toArray(Predicate[]::new);

        query.select(addressTokenBalanceRoot).where(criteriaBuilder.or(predicates));
        return entityManager.createQuery(query).getResultList();
    }

    private static Predicate buildAddressTokenBalancePredicate(
            CriteriaBuilder criteriaBuilder, Join<AddressTokenBalance, Address> addressJoin,
            Root<AddressTokenBalance> addressTokenBalanceRoot,
            Pair<String, String> addressFingerprintPair) {
        String address = addressFingerprintPair.getFirst();
        String fingerprint = addressFingerprintPair.getSecond();
        Predicate addressEquals = criteriaBuilder.equal(addressJoin.get("address"), address);
        Predicate fingerprintEquals = criteriaBuilder
                .equal(addressTokenBalanceRoot.get("fingerprint"), fingerprint);

        return criteriaBuilder.and(addressEquals, fingerprintEquals);
    }

    private static Predicate buildAddressTokenBalancePredicateWithIdPair(
            CriteriaBuilder criteriaBuilder, Join<AddressTokenBalance, Address> addressJoin,
            Root<AddressTokenBalance> addressTokenBalanceRoot,
            Pair<Long, String> addressMultiAssetIdPair) {
        Long addressId = addressMultiAssetIdPair.getFirst();
        String fingerprint = addressMultiAssetIdPair.getSecond();
        Predicate addressEquals = criteriaBuilder.equal(addressJoin.get("id"), addressId);
        Predicate fingerprintEquals = criteriaBuilder
                .equal(addressTokenBalanceRoot.get("fingerprint"), fingerprint);

        return criteriaBuilder.and(addressEquals, fingerprintEquals);
    }
}
