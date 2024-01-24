package org.cardanofoundation.ledgersync.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ParamProposal.class)
public abstract class ParamProposal_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<ParamProposal, BigInteger> maxBlockSize;
	public static volatile SingularAttribute<ParamProposal, BigInteger> keyDeposit;
	public static volatile SingularAttribute<ParamProposal, Integer> maxCollateralInputs;
	public static volatile SingularAttribute<ParamProposal, BigInteger> maxValSize;
	public static volatile SingularAttribute<ParamProposal, BigInteger> maxBhSize;
	public static volatile SingularAttribute<ParamProposal, BigInteger> minFeeB;
	public static volatile SingularAttribute<ParamProposal, BigInteger> minFeeA;
	public static volatile SingularAttribute<ParamProposal, Double> influence;
	public static volatile SingularAttribute<ParamProposal, Integer> protocolMinor;
	public static volatile SingularAttribute<ParamProposal, BigInteger> poolDeposit;
	public static volatile SingularAttribute<ParamProposal, BigInteger> maxTxSize;
	public static volatile SingularAttribute<ParamProposal, Double> treasuryGrowthRate;
	public static volatile SingularAttribute<ParamProposal, String> entropy;
	public static volatile SingularAttribute<ParamProposal, Integer> epochNo;
	public static volatile SingularAttribute<ParamProposal, Double> priceMem;
	public static volatile SingularAttribute<ParamProposal, BigInteger> maxTxExSteps;
	public static volatile SingularAttribute<ParamProposal, BigInteger> maxBlockExSteps;
	public static volatile SingularAttribute<ParamProposal, Tx> registeredTx;
	public static volatile SingularAttribute<ParamProposal, Integer> collateralPercent;
	public static volatile SingularAttribute<ParamProposal, String> key;
	public static volatile SingularAttribute<ParamProposal, Double> monetaryExpandRate;
	public static volatile SingularAttribute<ParamProposal, Double> decentralisation;
	public static volatile SingularAttribute<ParamProposal, BigInteger> maxEpoch;
	public static volatile SingularAttribute<ParamProposal, Integer> protocolMajor;
	public static volatile SingularAttribute<ParamProposal, BigInteger> minPoolCost;
	public static volatile SingularAttribute<ParamProposal, BigInteger> maxTxExMem;
	public static volatile SingularAttribute<ParamProposal, CostModel> costModel;
	public static volatile SingularAttribute<ParamProposal, BigInteger> minUtxoValue;
	public static volatile SingularAttribute<ParamProposal, BigInteger> maxBlockExMem;
	public static volatile SingularAttribute<ParamProposal, BigInteger> coinsPerUtxoSize;
	public static volatile SingularAttribute<ParamProposal, BigInteger> optimalPoolCount;
	public static volatile SingularAttribute<ParamProposal, Long> registeredTxId;
	public static volatile SingularAttribute<ParamProposal, Long> costModelId;
	public static volatile SingularAttribute<ParamProposal, Double> priceStep;

	public static final String MAX_BLOCK_SIZE = "maxBlockSize";
	public static final String KEY_DEPOSIT = "keyDeposit";
	public static final String MAX_COLLATERAL_INPUTS = "maxCollateralInputs";
	public static final String MAX_VAL_SIZE = "maxValSize";
	public static final String MAX_BH_SIZE = "maxBhSize";
	public static final String MIN_FEE_B = "minFeeB";
	public static final String MIN_FEE_A = "minFeeA";
	public static final String INFLUENCE = "influence";
	public static final String PROTOCOL_MINOR = "protocolMinor";
	public static final String POOL_DEPOSIT = "poolDeposit";
	public static final String MAX_TX_SIZE = "maxTxSize";
	public static final String TREASURY_GROWTH_RATE = "treasuryGrowthRate";
	public static final String ENTROPY = "entropy";
	public static final String EPOCH_NO = "epochNo";
	public static final String PRICE_MEM = "priceMem";
	public static final String MAX_TX_EX_STEPS = "maxTxExSteps";
	public static final String MAX_BLOCK_EX_STEPS = "maxBlockExSteps";
	public static final String REGISTERED_TX = "registeredTx";
	public static final String COLLATERAL_PERCENT = "collateralPercent";
	public static final String KEY = "key";
	public static final String MONETARY_EXPAND_RATE = "monetaryExpandRate";
	public static final String DECENTRALISATION = "decentralisation";
	public static final String MAX_EPOCH = "maxEpoch";
	public static final String PROTOCOL_MAJOR = "protocolMajor";
	public static final String MIN_POOL_COST = "minPoolCost";
	public static final String MAX_TX_EX_MEM = "maxTxExMem";
	public static final String COST_MODEL = "costModel";
	public static final String MIN_UTXO_VALUE = "minUtxoValue";
	public static final String MAX_BLOCK_EX_MEM = "maxBlockExMem";
	public static final String COINS_PER_UTXO_SIZE = "coinsPerUtxoSize";
	public static final String OPTIMAL_POOL_COUNT = "optimalPoolCount";
	public static final String REGISTERED_TX_ID = "registeredTxId";
	public static final String COST_MODEL_ID = "costModelId";
	public static final String PRICE_STEP = "priceStep";

}

