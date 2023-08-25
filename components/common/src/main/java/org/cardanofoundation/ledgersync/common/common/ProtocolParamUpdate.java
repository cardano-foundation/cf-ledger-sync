package org.cardanofoundation.ledgersync.common.common;

import org.cardanofoundation.ledgersync.common.common.cost.mdl.CostModels;
import java.util.List;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder

public class ProtocolParamUpdate {

  private Integer minFeeA; //0
  private Integer minFeeB; //1
  private Integer maxBlockSize; //2
  private Integer maxTxSize; //3
  private Integer maxBlockHeaderSize; //4
  private BigInteger keyDeposit; //5
  private BigInteger poolDeposit; //6
  private Integer maxEpoch; //7
  private Integer optimalPoolCount; //8
  private BigDecimal poolPledgeInfluence; //rational //9
  private BigDecimal expansionRate; //unit interval //10
  private BigDecimal treasuryGrowthRate; //11
  private BigDecimal decentralisationParam; //12
  private List<Object> extraEntropy;  //13
  private Integer protocolMajorVer; //14
  private Integer protocolMinorVer; //14
  private BigInteger minUtxo;  //15

  private BigInteger minPoolCost; //16
  private BigInteger adaPerUtxoByte; //17
  //private String nonce;

  //Alonzo changes
  private CostModels costModels; //18

  //ex_unit_prices
  private BigDecimal priceMem; //19
  private BigDecimal priceStep; //19

  //max tx ex units
  private BigInteger maxTxExMem; //20
  private BigInteger maxTxExSteps; //20

  //max block ex units
  private BigInteger maxBlockExMem; //21
  private BigInteger maxBlockExSteps; //21

  private Long maxValSize; //22

  private Integer collateralPercent; //23
  private Integer maxCollateralInputs; //24

/*    //Cost per UTxO word for Alonzo.
    //Cost per UTxO byte for Babbage and later.
   private String coinsPerUtxoSize;
   @Deprecated
   private String coinsPerUtxoWord;*/
}
