package org.cardanofoundation.ledgersync.verifier.data.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rest.koios.client.backend.api.address.AddressService;
import rest.koios.client.backend.api.block.BlockService;
import rest.koios.client.backend.api.transactions.TransactionsService;
import rest.koios.client.backend.factory.BackendFactory;
import rest.koios.client.backend.factory.BackendService;

@Configuration
public class KoiosConfig {

    /**
     * Creates a bean for accessing the Koios preprod backend service.
     *
     * @return BackendService instance for Koios preprod.
     */
    @Bean
    public BackendService createBackendServiceBean() {
        return BackendFactory.getKoiosPreprodService();
    }

    /**
     * Creates a bean for accessing the address service provided by the backend service.
     *
     * @param backendService The backend service instance.
     * @return AddressService instance for interacting with address-related data.
     */
    @Bean
    public AddressService createAddressServiceBean(BackendService backendService) {
        return backendService.getAddressService();
    }

    /**
     * Creates a bean for accessing the transactions service provided by the backend service.
     *
     * @param backendService The backend service instance.
     * @return TransactionsService instance for interacting with transaction-related data.
     */
    @Bean
    public TransactionsService createTransactionServiceBean(BackendService backendService) {
        return backendService.getTransactionsService();
    }

    /**
     * Creates a bean for accessing the block service provided by the backend service.
     *
     * @param backendService The backend service instance.
     * @return BlockService instance for interacting with block-related data.
     */
    @Bean
    public BlockService createBlockServiceBean(BackendService backendService) {
        return backendService.getBlockService();
    }
}

