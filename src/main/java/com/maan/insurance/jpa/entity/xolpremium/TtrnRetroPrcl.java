package com.maan.insurance.jpa.entity.xolpremium;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="TTRN_RETRO_PRCL")


public class TtrnRetroPrcl implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    @Id
    @Column(name="RETRO_CONTRACT_NUMBER")
    private Long retroContractNumber;
    
    @Column(name="RETRO_BROKER")
    private String retroBroker;
    
    @Column(name="RETROCESSIONAIRE")
    private String retrocessionaire;
    
    @Column(name="PROPOSAL_NO")
    private Long proposalNo;
    
    @Column(name="CONTRACT_NO")
    private Long ContractNo;
    
    @Column(name="LAYER_NO")
    private Long layerNo;
    
    @Column(name="PRODUCT_ID")
    private Long productId;
    
    @Column(name="UWY")
    private String uwy;
    
    @Column(name="SPC")
    private String spc;
    
    @Column(name="TRANSACTION_NO")
    private Long transactionNo;
    
    @Column(name="TRANSACTION_MONTH_YEAR")
    private Date transactionMonthYear;
    
    @Column(name="TRANSACTION_TYPE")
    private String transactionType;
    
    @Column(name="CURRENCY_ID")
    private BigDecimal currencyId;
    
    @Column(name="EXCHANGE_RATE")
    private BigDecimal exchangeRate;
    
    @Column(name="RETRO_CURRENCY_ID")
    private BigDecimal retroCurrencyId;
    
    @Column(name="RETRO_EXCHANGE_RATE")
    private BigDecimal retroExchangeRate;
    
    @Column(name="ADJUSTMENT_PREMIUM_OC")
    private BigDecimal adjustmentPremiumOc;
    
    @Column(name="M_DPREMIUM_OC")
    private BigDecimal mDpremiumOc;
    
    @Column(name="PREMIUM_QUOTASHARE_OC")
    private BigDecimal premiumQuotashareOc;
    
    @Column(name="PREMIUM_SURPLUS_OC")
    private BigDecimal premiumSurplusOc;
    
    @Column(name="REC_PREMIUM_OC")
    private BigDecimal  recPremiumOc;
    
    @Column(name="XL_COST_OC")
    private BigDecimal xlCostOc;
    
    @Column(name="PREMIUM_PORTFOLIOIN_OC")
    private BigDecimal premiumPortfolioinOc;
    
    @Column(name="PREMIUM_PORTFOLIOOUT_OC")
    private BigDecimal premiumPortfoliooutOc;
    
    @Column(name="INTEREST_OC")
    private BigDecimal interestOc;
    
    @Column(name="PREMIUM_RESERVE_RETAINED_OC")
    private BigDecimal premiumReserveRetainedOc;
    
    @Column(name="PREMIUMRESERVE_QUOTASHARE_OC")
    private BigDecimal premiumreserveQuotashareOc;
    
    @Column(name="PREMIUM_RESERVE_REALSED_OC")
    private BigDecimal premiumReserveRealsedOc;
    
    @Column(name="PREMIUM_RESERVE_RELEASE_OC")
    private BigDecimal premiumReserveReleaseOc;
    
    @Column(name="LOSS_RESERVERETAINED_OC")
    private BigDecimal  lossReserveretainedOc;
    
    @Column(name="LOSS_RESERVE_RELEASED_OC")
    private BigDecimal lossReserveReleasedOc;
    
    @Column(name="COMMISSION_QUOTASHARE_OC")
    private BigDecimal commissionQuotashareOc;
    
    @Column(name="COMMISSION_SURPLUS_OC")
    private BigDecimal commissionSurplusOc;
    
    @Column(name="BROKERAGE_AMT_OC")
    private BigDecimal brokerageAmtOc;
    
    @Column(name="TAX_AMT_OC")
    private BigDecimal taxAmtOc;
    
    @Column(name="OVERRIDER_AMT_OC")
    private BigDecimal overriderAmtOc;
    
    @Column(name="PROFIT_COMMISSION_OC")
    private BigDecimal profitCommissionOc;
    
    @Column(name="OTHER_COST_OC")
    private BigDecimal otherCostOc;
    
    @Column(name="CASH_LOSSPAID_OC")
    private BigDecimal cashLosspaidOc;
    
    @Column(name="CLAIMS_PAID_OC")
    private BigDecimal claimsPaidOc;
    
    @Column(name="CASH_LOSS_CREDIT_OC")
    private BigDecimal cashLossCreditOc;
    
    @Column(name="CLAIM_PORTFOLIOIN_OC")
    private BigDecimal  claimPortfolioinOc;
    
    @Column(name="CLAIM_PORTFOLIO_OUT_OC")
    private BigDecimal claimPortfolioOutOc;
    
    @Column(name="OW_OVERRIDER_OC")
    private BigDecimal owOverriderOc;
    
    @Column(name="NET_DUE_OC")
    private BigDecimal netDueOc;
    
    @Column(name="OSCLAIM_LOSSUPDATE_OC")
    private BigDecimal osclaimLossupdateOc;
    
    @Column(name="ADJUSTMENT_PREMIUM_DC")
    private BigDecimal adjustmentPremiumDc;
    
    @Column(name="M_DPREMIUM_DC")
    private BigDecimal mDpremiumDc;
    
    @Column(name="PREMIUM_QUOTASHARE_DC")
    private BigDecimal premiumQuotashareDc;
    
    @Column(name="PREMIUM_SURPLUS_DC")
    private BigDecimal premiumSurplusDc;
    
    @Column(name="REC_PREMIUM_DC")
    private BigDecimal recPremiumDc;
    
    @Column(name="XL_COST_DC")
    private BigDecimal xlCostDc;
    
    @Column(name="PREMIUM_PORTFOLIOIN_DC")
    private BigDecimal premiumPortfolioinDc;
    
    @Column(name="PREMIUM_PORTFOLIOOUT_DC")
    private BigDecimal premiumPortfoliooutDc;
    
    @Column(name="INTEREST_DC")
    private BigDecimal interestDc;
    
    @Column(name="PREMIUM_RESERVE_RETAINED_DC")
    private BigDecimal  premiumReserveRetainedDc;
    
    @Column(name="PREMIUMRESERVE_QUOTASHARE_DC")
    private BigDecimal premiumreserveQuotashareDc;
    
    @Column(name="PREMIUM_RESERVE_REALSED_DC")
    private BigDecimal premiumReserveRealsedDc;
    
    @Column(name="PREMIUM_RESERVE_RELEASE_DC")
    private BigDecimal premiumReserveReleaseDc;
    
    @Column(name="LOSS_RESERVERETAINED_DC")
    private BigDecimal lossReserveretainedDc;
    
    @Column(name="LOSS_RESERVE_RELEASED_DC")
    private BigDecimal lossReserveReleasedDc;
    
    @Column(name="COMMISSION_QUOTASHARE_DC")
    private BigDecimal  commissionQuotashareDc;
    
    @Column(name="COMMISSION_SURPLUS_DC")
    private BigDecimal commissionSurplusDc;
    
    @Column(name="BROKERAGE_AMT_DC")
    private BigDecimal brokerageAmtDc;
    
    @Column(name="TAX_AMT_DC")
    private BigDecimal taxAmtDc;
    
    @Column(name="OVERRIDER_AMT_DC")
    private BigDecimal overriderAmtDc;
    
    @Column(name="PROFIT_COMMISSION_DC")
    private BigDecimal profitCommissionDc;
    
    @Column(name="OTHER_COST_DC")
    private BigDecimal  otherCostDc;
    
    @Column(name="CASH_LOSSPAID_DC")
    private BigDecimal cashLosspaidDc;
    
    @Column(name="CLAIMS_PAID_DC")
    private BigDecimal  claimsPaidDc;
    
    @Column(name="CASH_LOSS_CREDIT_DC")
    private BigDecimal cashLossCreditDc;
    
    @Column(name="CLAIM_PORTFOLIOIN_DC")
    private BigDecimal claimPortfolioinDc;
    
    @Column(name="CLAIM_PORTFOLIO_OUT_DC")
    private BigDecimal claimPortfolioOutDc;
    
    @Column(name="OW_OVERRIDER_DC")
    private BigDecimal owOverriderDc;
    
    @Column(name="NET_DUE_DC")
    private BigDecimal netDueDc;
    
    @Column(name="OSCLAIM_LOSSUPDATE_DC")
    private BigDecimal  osclaimLossupdateDc;
    
    @Column(name="ADJUSTMENT_PREMIUM_RC")
    private BigDecimal adjustmentPremiumRc;
    
    @Column(name="M_DPREMIUM_RC")
    private BigDecimal mDpremiumRc;
    
    @Column(name="PREMIUM_QUOTASHARE_RC")
    private BigDecimal premiumQuotashareRc;
    
    @Column(name="PREMIUM_SURPLUS_RC")
    private BigDecimal premiumSurplusRc;
    
    @Column(name="REC_PREMIUM_RC")
    private BigDecimal recPremiumRc;
    
    @Column(name="XL_COST_RC")
    private BigDecimal xlCostRc;
    
    @Column(name="PREMIUM_PORTFOLIOIN_RC")
    private BigDecimal premiumPortfolioinRc;
    
    @Column(name="PREMIUM_PORTFOLIOOUT_RC")
    private BigDecimal premiumPortfoliooutRc;
    
    @Column(name="INTEREST_RC")
    private BigDecimal interestRc;
    
    @Column(name="PREMIUM_RESERVE_RETAINED_RC")
    private BigDecimal premiumReserveRetainedRc;
    
    @Column(name="PREMIUMRESERVE_QUOTASHARE_RC")
    private BigDecimal premiumreserveQuotashareRc;
    
    @Column(name="PREMIUM_RESERVE_REALSED_RC")
    private BigDecimal premiumReserveRealsedRc;
    
    @Column(name="PREMIUM_RESERVE_RELEASE_RC")
    private BigDecimal premiumReserveReleaseRc;
    
    @Column(name="LOSS_RESERVERETAINED_RC")
    private BigDecimal lossReserveretainedRc;
    
    @Column(name="LOSS_RESERVE_RELEASED_RC")
    private BigDecimal lossReserveReleasedRc;
    
    @Column(name="COMMISSION_QUOTASHARE_RC")
    private BigDecimal commissionQuotashareRc;
    
    @Column(name="COMMISSION_SURPLUS_RC")
    private BigDecimal commissionSurplusRc;
    
    @Column(name="BROKERAGE_AMT_RC")
    private BigDecimal brokerageAmtRc;
    
    @Column(name="TAX_AMT_RC")
    private BigDecimal taxAmtRc;
    
    @Column(name="OVERRIDER_AMT_RC")
    private BigDecimal overriderAmtRc;
    
    @Column(name="PROFIT_COMMISSION_RC")
    private BigDecimal profitCommissionRc;
    
    @Column(name="OTHER_COST_RC")
    private BigDecimal otherCostRc;
    
    @Column(name="CASH_LOSSPAID_RC")
    private BigDecimal cashLosspaidRc;
    
    @Column(name="CLAIMS_PAID_RC")
    private BigDecimal claimsPaidRc;
    
    @Column(name="CASH_LOSS_CREDIT_RC")
    private BigDecimal cashLossCreditRc;
    
    @Column(name="CLAIM_PORTFOLIOIN_RC")
    private BigDecimal claimPortfolioinRc;
    
    @Column(name="CLAIM_PORTFOLIO_OUT_RC")
    private BigDecimal claimPortfolioOutRc;
    
    @Column(name="OW_OVERRIDER_RC")
    private BigDecimal owOverriderRc;
    
    @Column(name="NET_DUE_RC")
    private BigDecimal netDueRc;
    
    @Column(name="OSCLAIM_LOSSUPDATE_RC")
    private BigDecimal osclaimLossupdateRc;
    
    @Column(name="STATUS")
    private String status;
    
    @Column(name="ENTERING_MODE")
    private String enteringMode;
    
    @Column(name="ENTRY_DATE")
    private Timestamp entryDate;
    
    @Column(name="AMENDMENT_DATE")
    private Timestamp amendmentDate;
    
    @Column(name="AMEND_ID")
    private Long amendId;
    
    @Column(name="RETRO_TYPE")
    private String retroType;
    
    @Column(name="RETRO_PROPOSL_NO")
    private Long retroProposlNo;
    
    @Column(name="RETRO_LAYER_NO")
    private Long retroLayerNo;
    
    @Column(name="RETRO_PERCENTAGE")
    private String retroPercentage;
    
    @Column(name="BRANCH_CODE")
    private String branchCode;
    
    @Column(name="RI_PERCENTAGE")
    private String riPercentage;
    
    @Column(name="OW_OVERRIDER_PERCENTAGE")
    private String owOverriderPercentage;
    
    @Column(name="PROCESS_ID")
    private String processId;
    
    @Column(name="START_DATE")
    private Date startDate;
    
    @Column(name="END_DATE")
    private Date endDate;
    
    @Column(name="PROCESS_SYS_DATE")
    private Date processSysDate;
    
    @Column(name="RCP_EXCHANGERATE")
    private BigDecimal rcpExchangerate;
    
    @Column(name="ADJUSTMENT_PREMIUM_RCP")
    private BigDecimal adjustmentPremiumRcp;
    
    @Column(name="M_DPREMIUM_RCP")
    private BigDecimal mDpremiumRcp;
    
    @Column(name="PREMIUM_QUOTASHARE_RCP")
    private BigDecimal premiumQuotashareRcp;
    
    @Column(name="PREMIUM_SURPLUS_RCP")
    private BigDecimal  premiumSurplusRcp;
    
    @Column(name="REC_PREMIUM_RCP")
    private BigDecimal recPremiumRcp;
    
    @Column(name="XL_COST_RCP")
    private BigDecimal  xlCostRcp;
    
    @Column(name="PREMIUM_PORTFOLIOIN_RCP")
    private BigDecimal premiumPortfolioinRcp;
    
    @Column(name="PREMIUM_PORTFOLIOOUT_RCP")
    private BigDecimal premiumPortfoliooutRcp;
    
    @Column(name="INTEREST_RCP")
    private BigDecimal interestRcp;
    
    @Column(name="PREMIUM_RESERVE_RETAINED_RCP")
    private BigDecimal premiumReserveRetainedRcp;
    
    @Column(name="PREMIUMRESERVE_QUOTASHARE_RCP")
    private BigDecimal premiumreserveQuotashareRcp;
    
    @Column(name="PREMIUM_RESERVE_REALSED_RCP")
    private BigDecimal premiumReserveRealsedRcp;
    
    @Column(name="PREMIUM_RESERVE_RELEASE_RCP")
    private BigDecimal  premiumReserveReleaseRcp;
    
    @Column(name="LOSS_RESERVERETAINED_RCP")
    private BigDecimal  lossReserveretainedRcp;
    
    @Column(name="LOSS_RESERVE_RELEASED_RCP")
    private BigDecimal lossReserveReleasedRcp;
    
    @Column(name="COMMISSION_QUOTASHARE_RCP")
    private BigDecimal commissionQuotashareRcp;
    
    @Column(name="COMMISSION_SURPLUS_RCP")
    private BigDecimal commissionSurplusRcp;
    
    @Column(name="BROKERAGE_AMT_RCP")
    private BigDecimal  brokerageAmtRcp;
    
    @Column(name="TAX_AMT_RCP")
    private BigDecimal taxAmtRcp;
    
    @Column(name="OVERRIDER_AMT_RCP")
    private BigDecimal overriderAmtRcp;
    
    @Column(name="PROFIT_COMMISSION_RCP")
    private BigDecimal profitCommissionRcp;
    
    @Column(name="OTHER_COST_RCP")
    private BigDecimal otherCostRcp;
    
    @Column(name="CASH_LOSSPAID_RCP")
    private BigDecimal cashLosspaidRcp;
    
    @Column(name="CLAIMS_PAID_RCP")
    private BigDecimal claimsPaidRcp;
    
    @Column(name="CASH_LOSS_CREDIT_RCP")
    private BigDecimal cashLossCreditRcp;
    
    @Column(name="CLAIM_PORTFOLIOIN_RCP")
    private BigDecimal claimPortfolioinRcp;
    
    @Column(name="CLAIM_PORTFOLIO_OUT_RCP")
    private BigDecimal claimPortfolioOutRcp;
    
    @Column(name="OW_OVERRIDER_RCP")
    private BigDecimal owOverriderRcp;
    
    @Column(name="NET_DUE_RCP")
    private BigDecimal netDueRcp;
    
    @Column(name="OSCLAIM_LOSSUPDATE_RCP")
    private BigDecimal osclaimLossupdateRcp;
    
    @Column(name="SOA_FREQUENCY")
    private String soaFrequency;
    
    @Column(name="SUB_CLASS")
    private Long subClass;
    
    @Column(name="TDS_OC")
    private BigDecimal tdsOc;
    
    @Column(name="TDS_DC")
    private BigDecimal tdsDc;
    
    @Column(name="ST_OC")
    private BigDecimal  stOc;
    
    @Column(name="ST_DC")
    private BigDecimal stDc;
    
    @Column(name="BONUS_OC")
    private BigDecimal bonusOc;
    
    @Column(name="BONUS_DC")
    private BigDecimal bonusDc;
    
    @Column(name="PREMIUM_CLASS")
    private String premiumClass;
    
    @Column(name="CLASS")
    private String className;
    
    @Column(name="RETRO_RSK_DUMMY_TREATY")
    private String retroRskDummyTreaty;
    
    @Column(name="LPC_OC")
    private BigDecimal lpcOc;
    
    @Column(name="LPC_DC")
    private BigDecimal lpcDc;
    
    @Column(name="LPC_RC")
    private BigDecimal lpcRc;
    
    @Column(name="LPC_RCP")
    private BigDecimal lpcRcp;
    
    @Column(name="RETRO_EX_RATE_TYPE")
    private String retroExRateType;
    
    @Column(name="RSK_FIXED_RATE")
    private BigDecimal rskFixedRate;
    
    @Column(name="INWARD_CONTRACT_AMEND_ID")
    private Long inwardContractAmendId;
    
    @Column(name="RETRO_CONTRACT_AMEND_ID")
    private Long retroContractAmendId;
    
    @Column(name="BONUS_RC")
    private BigDecimal  bonusRc;
    
    @Column(name="BONUS_RCP")
    private BigDecimal bonusRcp;
    
    @Column(name="ST_RC")
    private BigDecimal stRc;
    
    @Column(name="ST_RCP")
    private BigDecimal stRcp;
    
    @Column(name="TDS_RC")
    private BigDecimal  tdsRc;
    
    @Column(name="TDS_RCP")
    private BigDecimal tdsRcp;
    
    @Column(name="SC_COMM_OC")
    private BigDecimal  scCommOc;
    
    @Column(name="SC_COMM_DC")
    private BigDecimal scCommDc;
    
    @Column(name="SC_COMM_RC")
    private BigDecimal scCommRc;
    
    @Column(name="SC_COMM_RCP")
    private BigDecimal scCommRcp;
    
    @Column(name="WITH_HOLDING_TAX_OC")
    private BigDecimal withHoldingTaxOc;
    
    @Column(name="WITH_HOLDING_TAX_DC")
    private BigDecimal withHoldingTaxDc;
    
    @Column(name="WITH_HOLDING_TAX_RC")
    private BigDecimal withHoldingTaxRc;
    
    @Column(name="WITH_HOLDING_TAX_RCP")
    private BigDecimal withHoldingTaxRcp;
    
    @Column(name="REFERENCE")
    private String reference;
    
    @Column(name="TREATY_NAME")
    private String  treatyName;
    
    @Column(name="REMARKS")
    private String remarks;
    
    @Column(name="BUSINESS_TYPE")
    private String businessType;

    
}



