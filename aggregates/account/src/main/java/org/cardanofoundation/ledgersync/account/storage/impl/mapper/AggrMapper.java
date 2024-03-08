package org.cardanofoundation.ledgersync.account.storage.impl.mapper;

import org.cardanofoundation.ledgersync.account.domain.AddressTxAmount;
import org.cardanofoundation.ledgersync.account.storage.impl.model.AddressTxAmountEntity;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default")
@DecoratedWith(AggrMapperDecorator.class)
public interface AggrMapper {
    AggrMapper INSTANCE = Mappers.getMapper(AggrMapper.class);

    AddressTxAmount toAddressTxAmount(AddressTxAmountEntity entity);
    AddressTxAmountEntity toAddressTxAmountEntity(AddressTxAmount addressTxAmount);
}

