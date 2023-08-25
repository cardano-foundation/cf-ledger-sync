package org.cardanofoundation.ledgersync.explorerconsumer.repository;

import org.cardanofoundation.explorer.consumercommon.entity.TxChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface TxChartRepository extends JpaRepository<TxChart, Long> {

    List<TxChart> findAllByMinuteBetween(BigInteger minuteStart, BigInteger minuteEnd);

}
