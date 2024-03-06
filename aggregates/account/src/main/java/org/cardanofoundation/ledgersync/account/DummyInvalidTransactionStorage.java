package org.cardanofoundation.ledgersync.account;

import com.bloxbean.cardano.yaci.store.utxo.domain.InvalidTransaction;
import com.bloxbean.cardano.yaci.store.utxo.storage.impl.InvalidTransactionStorageImpl;
import com.bloxbean.cardano.yaci.store.utxo.storage.impl.repository.InvalidTransactionRepository;
import org.springframework.stereotype.Component;

//TODO -- remove this class later
//Overrides the save method to do nothing to fix the NUL char issue in invalid transaction
//https://github.com/bloxbean/yaci-store/issues/209
@Component
public class DummyInvalidTransactionStorage extends InvalidTransactionStorageImpl {
    public DummyInvalidTransactionStorage(InvalidTransactionRepository repository) {
        super(repository);
    }

    @Override
    public InvalidTransaction save(InvalidTransaction invalidTransaction) {
        return invalidTransaction; //do nothing
    }
}
