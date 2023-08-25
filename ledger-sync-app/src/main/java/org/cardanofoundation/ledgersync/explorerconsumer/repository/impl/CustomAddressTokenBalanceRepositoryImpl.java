package org.cardanofoundation.ledgersync.explorerconsumer.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.cardanofoundation.explorer.consumercommon.entity.*;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.CustomAddressTokenBalanceRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomAddressTokenBalanceRepositoryImpl implements
        CustomAddressTokenBalanceRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Collection<AddressTokenBalance> findAllByAddressFingerprintPairIn(
            Collection<Pair<String, String>> addressFingerprintPairs) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(AddressTokenBalance.class);
        Root<AddressTokenBalance> addressTokenBalanceRoot = query.from(AddressTokenBalance.class);
        Join<AddressTokenBalance, Address> addressJoin =
                addressTokenBalanceRoot.join(AddressTokenBalance_.address);
        Join<AddressTokenBalance, MultiAsset> multiAssetJoin =
                addressTokenBalanceRoot.join(AddressTokenBalance_.multiAsset);

        Predicate[] predicates = addressFingerprintPairs.stream()
                .map(addressFingerprintPair ->
                        buildAddressTokenBalancePredicate(
                                criteriaBuilder, addressJoin, multiAssetJoin, addressFingerprintPair))
                .toArray(Predicate[]::new);

        query.select(addressTokenBalanceRoot).where(criteriaBuilder.or(predicates));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Collection<AddressTokenBalance> findAllByAddressMultiAssetIdPairIn(
            Collection<Pair<Long, Long>> addressMultiAssetIdPairs) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(AddressTokenBalance.class);
        Root<AddressTokenBalance> addressTokenBalanceRoot = query.from(AddressTokenBalance.class);
        Join<AddressTokenBalance, Address> addressJoin =
                addressTokenBalanceRoot.join(AddressTokenBalance_.address);
        Join<AddressTokenBalance, MultiAsset> multiAssetJoin =
                addressTokenBalanceRoot.join(AddressTokenBalance_.multiAsset);

        Predicate[] predicates = addressMultiAssetIdPairs.stream()
                .map(addressMultiAssetIdPair ->
                        buildAddressTokenBalancePredicateWithIdPair(
                                criteriaBuilder, addressJoin, multiAssetJoin, addressMultiAssetIdPair))
                .toArray(Predicate[]::new);

        query.select(addressTokenBalanceRoot).where(criteriaBuilder.or(predicates));
        return entityManager.createQuery(query).getResultList();
    }

    private static Predicate buildAddressTokenBalancePredicate(
            CriteriaBuilder criteriaBuilder, Join<AddressTokenBalance, Address> addressJoin,
            Join<AddressTokenBalance, MultiAsset> multiAssetJoin,
            Pair<String, String> addressFingerprintPair) {
        String address = addressFingerprintPair.getFirst();
        String fingerprint = addressFingerprintPair.getSecond();
        Predicate addressEquals = criteriaBuilder.equal(addressJoin.get(Address_.address), address);
        Predicate fingerprintEquals = criteriaBuilder
                .equal(multiAssetJoin.get(MultiAsset_.fingerprint), fingerprint);

        return criteriaBuilder.and(addressEquals, fingerprintEquals);
    }

    private static Predicate buildAddressTokenBalancePredicateWithIdPair(
            CriteriaBuilder criteriaBuilder, Join<AddressTokenBalance, Address> addressJoin,
            Join<AddressTokenBalance, MultiAsset> multiAssetJoin,
            Pair<Long, Long> addressMultiAssetIdPair) {
        Long addressId = addressMultiAssetIdPair.getFirst();
        Long multiAssetId = addressMultiAssetIdPair.getSecond();
        Predicate addressEquals = criteriaBuilder.equal(addressJoin.get(BaseEntity_.id), addressId);
        Predicate fingerprintEquals = criteriaBuilder
                .equal(multiAssetJoin.get(BaseEntity_.id), multiAssetId);

        return criteriaBuilder.and(addressEquals, fingerprintEquals);
    }
}
