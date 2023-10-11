package org.cardanofoundation.ledgersync.scheduler.service;


import org.cardanofoundation.ledgersync.scheduler.dto.PoolData;

import java.util.List;

public interface PoolOfflineDataStoringService {

  /**
   * Saving the fetched success offline data
   *
   * @param successPools the fetched success pools
   */
  void saveSuccessPoolOfflineData(List<PoolData> successPools);


  /**
   * Saving the fetched fail offline data
   *
   * @param failedPools the fetched fail pools
   */
  void saveFailOfflineData(List<PoolData> failedPools);
}
