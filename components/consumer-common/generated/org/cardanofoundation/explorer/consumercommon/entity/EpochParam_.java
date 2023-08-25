package org.cardanofoundation.explorer.consumercommon.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EpochParam.class)
public abstract class EpochParam_ extends org.cardanofoundation.explorer.consumercommon.entity.BaseEntity_ {

	public static volatile SingularAttribute<EpochParam, Integer> maxBlockSize;
	public static volatile SingularAttribute<EpochParam, BigInteger> keyDeposit;
	public static volatile SingularAttribute<EpochParam, Integer> maxCollateralInputs;
	public static volatile SingularAttribute<EpochParam, BigInteger> maxValSize;
	public static volatile SingularAttribute<EpochParam, Integer> maxBhSize;
	public static volatile SingularAttribute<EpochParam, Integer> minFeeB;
	public static volatile SingularAttribute<EpochParam, Integer> minFeeA;
	public static volatile SingularAttribute<EpochParam, Double> influence;
	public static volatile SingularAttribute<EpochParam, Integer> protocolMinor;
	public static volatile SingularAttribute<EpochParam, BigInteger> poolDeposit;
	public static volatile SingularAttribute<EpochParam, Integer> maxTxSize;
	public static volatile SingularAttribute<EpochParam, Double> treasuryGrowthRate;
	public static volatile SingularAttribute<EpochParam, Integer> epochNo;
	public static volatile SingularAttribute<EpochParam, Double> priceMem;
	public static volatile SingularAttribute<EpochParam, BigInteger> maxTxExSteps;
	public static volatile SingularAttribute<EpochParam, Block> block;
	public static volatile SingularAttribute<EpochParam, BigInteger> maxBlockExSteps;
	public static volatile SingularAttribute<EpochParam, Integer> collateralPercent;
	public static volatile SingularAttribute<EpochParam, Double> monetaryExpandRate;
	public static volatile SingularAttribute<EpochParam, Double> decentralisation;
	public static volatile SingularAttribute<EpochParam, Integer> maxEpoch;
	public static volatile SingularAttribute<EpochParam, Integer> protocolMajor;
	public static volatile SingularAttribute<EpochParam, String> nonce;
	public static volatile SingularAttribute<EpochParam, BigInteger> minPoolCost;
	public static volatile SingularAttribute<EpochParam, BigInteger> maxTxExMem;
	public static volatile SingularAttribute<EpochParam, CostModel> costModel;
	public static volatile SingularAttribute<EpochParam, String> extraEntropy;
	public static volatile SingularAttribute<EpochParam, BigInteger> minUtxoValue;
	public static volatile SingularAttribute<EpochParam, BigInteger> maxBlockExMem;
	public static volatile SingularAttribute<EpochParam, BigInteger> coinsPerUtxoSize;
	public static volatile SingularAttribute<EpochParam, Integer> optimalPoolCount;
	public static volatile SingularAttribute<EpochParam, Double> priceStep;

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
	public static final String EPOCH_NO = "epochNo";
	public static final String PRICE_MEM = "priceMem";
	public static final String MAX_TX_EX_STEPS = "maxTxExSteps";
	public static final String BLOCK = "block";
	public static final String MAX_BLOCK_EX_STEPS = "maxBlockExSteps";
	public static final String COLLATERAL_PERCENT = "collateralPercent";
	public static final String MONETARY_EXPAND_RATE = "monetaryExpandRate";
	public static final String DECENTRALISATION = "decentralisation";
	public static final String MAX_EPOCH = "maxEpoch";
	public static final String PROTOCOL_MAJOR = "protocolMajor";
	public static final String NONCE = "nonce";
	public static final String MIN_POOL_COST = "minPoolCost";
	public static final String MAX_TX_EX_MEM = "maxTxExMem";
	public static final String COST_MODEL = "costModel";
	public static final String EXTRA_ENTROPY = "extraEntropy";
	public static final String MIN_UTXO_VALUE = "minUtxoValue";
	public static final String MAX_BLOCK_EX_MEM = "maxBlockExMem";
	public static final String COINS_PER_UTXO_SIZE = "coinsPerUtxoSize";
	public static final String OPTIMAL_POOL_COUNT = "optimalPoolCount";
	public static final String PRICE_STEP = "priceStep";

}

