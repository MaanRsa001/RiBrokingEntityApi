package com.maan.insurance.jpa.entity.treasury;

import java.sql.Date;

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
@Table(name="TTRN_RETRO_SOA")
public class TtrnRetroSoa {
	
	@Id
	@Column(name = "PROCESS_ID")
	private String processId;
	
	@Column(name = "START_DATE")
	private Date startDate;
	
	@Column(name = "END_DATE")
	private Date endDate;
	
	@Column(name = "TRANSACTION_NO")
	private Integer transactionNo;
	
	@Column(name = "TRANSACTION_DATE")
	private Date transactionDate;
	
	@Column(name = "RETRO_CONTRACT_NUMBER")
	private Integer retroContractNumber;
	
	@Column(name = "RETRO_BROKER")
	private String retroBroker;
	
	@Column(name = "RETROCESSIONAIRE")
	private String retrocessionaire;
	
	@Column(name = "RETRO_CURRENCY_ID")
	private Double retroCurrencyId;
	
	@Column(name = "RETRO_EXCHANGE_RATE")
	private Double retroExchangeRate;
	
	@Column(name = "ADJUSTMENT_PREMIUM_RCP")
	private Double adjustmentPremiumRcp;
	
	@Column(name = "M_DPREMIUM_RCP")
	private Double mDpremiumRcp;
	
	@Column(name = "PREMIUM_QUOTASHARE_RCP")
	private Double premiumQuotashareRcp;
	
	@Column(name = "PREMIUM_SURPLUS_RCP")
	private Double premiumSurplusRcp;
	
	@Column(name = "REC_PREMIUM_RCP")
	private Double recPremiumRcp;
	
	@Column(name = "XL_COST_RCP")
	private Double xlCostRcp;
	
	@Column(name = "PREMIUM_PORTFOLIOIN_RCP")
	private Double premiumPortfolioinRcp;
	
	@Column(name = "PREMIUM_PORTFOLIOOUT_RCP")
	private Double premiumPortfoliooutRcp;
	
	@Column(name = "INTEREST_RCP")
	private Double interestRcp;
	
	@Column(name = "PREMIUM_RESERVE_RETAINED_RCP")
	private Double premiumReserveRetainedRcp;
	
	@Column(name = "PREMIUMRESERVE_QUOTASHARE_RCP")
	private Double premiumreserveQuotashareRcp;
	
	@Column(name = "PREMIUM_RESERVE_REALSED_RCP")
	private Double premiumReserveRealsedRcp;
	
	@Column(name = "PREMIUM_RESERVE_RELEASE_RCP")
	private Double premiumReserveReleaseRcp;
	
	@Column(name = "LOSS_RESERVERETAINED_RCP")
	private Double lossReserveretainedRcp;
	
	@Column(name = "LOSS_RESERVE_RELEASED_RCP")
	private Double lossReserveReleasedRcp;
	
	@Column(name = "COMMISSION_QUOTASHARE_RCP")
	private Double commissionQuotashareRcp;
	
	@Column(name = "COMMISSION_SURPLUS_RCP")
	private Double commissionSurplusRcp;
	
	@Column(name = "BROKERAGE_AMT_RCP")
	private Double brokerageAmtRcp;
	
	@Column(name = "TAX_AMT_RCP")
	private Double taxAmtRcp;
	
	@Column(name = "OVERRIDER_AMT_RCP")
	private Double overriderAmtRcp;
	
	@Column(name = "PROFIT_COMMISSION_RCP")
	private Double profitCommissionRcp;
	
	@Column(name = "OTHER_COST_RCP")
	private Double otherCostRcp;
	
	@Column(name = "CASH_LOSSPAID_RCP")
	private Double cashLosspaidRcp;
	
	@Column(name = "CLAIMS_PAID_RCP")
	private Double claimsPaidRcp;
	
	@Column(name = "CASH_LOSS_CREDIT_RCP")
	private Double cashLossCreditRcp;
	
	@Column(name = "CLAIM_PORTFOLIOIN_RCP")
	private Double claimPortfolioinRcp;
	
	@Column(name = "CLAIM_PORTFOLIO_OUT_RCP")
	private Double claimPortfolioOutRcp;
	
	@Column(name = "OW_OVERRIDER_RCP")
	private Double owOverriderRcp;
	
	@Column(name = "NET_DUE_RCP")
	private Double netDueRcp;
	
	@Column(name = "OSCLAIM_LOSSUPDATE_RCP")
	private Double osclaimLossupdateRcp;
	
	@Column(name = "ALLOCATED_TILL_DATE")
	private Integer allocatedTillDate;
	
	@Column(name = "ALLOCATION_STATUS")
	private String allocationStatus;
	
	@Column(name = "ADJUSTMENT_PREMIUM_DC")
	private Double adjustmentPremiumDc;
	
	@Column(name = "M_DPREMIUM_DC")
	private Double mDpremiumDc;
	
	@Column(name = "PREMIUM_QUOTASHARE_DC")
	private Double premiumQuotashareDc;
	
	@Column(name = "PREMIUM_SURPLUS_DC")
	private Double premiumSurplusDc;
	
	@Column(name = "REC_PREMIUM_DC")
	private Double recPremiumDc;
	
	@Column(name = "XL_COST_DC")
	private Double xlCostDc;
	
	@Column(name = "PREMIUM_PORTFOLIOIN_DC")
	private Double premiumPortfolioinDc;
	
	@Column(name = "PREMIUM_PORTFOLIOOUT_DC")
	private Double premiumPortfoliooutDc;
	
	@Column(name = "INTEREST_DC")
	private Double interestDc;
	
	@Column(name = "PREMIUM_RESERVE_RETAINED_DC")
	private Double premiumReserveRetainedDc;
	
	@Column(name = "PREMIUMRESERVE_QUOTASHARE_DC")
	private Double premiumreserveQuotashareDc;
	
	@Column(name = "PREMIUM_RESERVE_REALSED_DC")
	private Double premiumReserveRealsedDc;
	
	@Column(name = "PREMIUM_RESERVE_RELEASE_DC")
	private Double premiumReserveReleaseDc;
	
	@Column(name = "LOSS_RESERVERETAINED_DC")
	private Double lossReserveretainedDc;
	
	@Column(name = "LOSS_RESERVE_RELEASED_DC")
	private Double lossReserveReleasedDc;
	
	@Column(name = "COMMISSION_QUOTASHARE_DC")
	private Double commissionQuotashareDc;
	
	@Column(name = "COMMISSION_SURPLUS_DC")
	private Double commissionSurplusDc;
	
	@Column(name = "BROKERAGE_AMT_DC")
	private Double brokerageAmtDc;
	
	@Column(name = "TAX_AMT_DC")
	private Double taxAmtDc;
	
	@Column(name = "OVERRIDER_AMT_DC")
	private Double overriderAmtDc;
	
	@Column(name = "PROFIT_COMMISSION_DC")
	private Double profitCommissionDc;
	
	@Column(name = "OTHER_COST_DC")
	private Double otherCostDc;
	
	@Column(name = "CASH_LOSSPAID_DC")
	private Double cashLosspaidDc;
	
	@Column(name = "CLAIMS_PAID_DC")
	private Double claimsPaidDc;
	
	@Column(name = "CASH_LOSS_CREDIT_DC")
	private Double cashLossCreditDc;
	
	@Column(name = "CLAIM_PORTFOLIOIN_DC")
	private Double claimPortfolioinDc;
	
	@Column(name = "CLAIM_PORTFOLIO_OUT_DC")
	private Double claimPortfolioOutDc;
	
	@Column(name = "OW_OVERRIDER_DC")
	private Double owOverriderDc;
	
	@Column(name = "NET_DUE_DC")
	private Double netDueDc;
	
	@Column(name = "OSCLAIM_LOSSUPDATE_DC")
	private Double osclaimLossupdateDc;
	
	@Column(name = "BRANCH_CODE")
	private String branchCode;
	
	@Column(name = "SOA_FREQUENCY")
	private String soaFrequency;
	
	@Column(name = "TDS_RCP")
	private Double tdsRcp;
	
	@Column(name = "TDS_DC")
	private Double tdsDc;
	
	@Column(name = "ST_RCP")
	private Double stRcp;
	
	@Column(name = "ST_DC")
	private Double stDc;
	
	@Column(name = "BONUS_RCP")
	private Double bonusRcp;
	
	@Column(name = "BONUS_DC")
	private Double bonusDc;
	
	@Column(name = "LPC_RCP")
	private Double lpcRcp;
	
	@Column(name = "LPC_DC")
	private Double lpcDc;
	
	@Column(name = "SC_COMM_DC")
	private Double scCommDc;
	
	@Column(name = "SC_COMM_RCP")
	private Double scCommRcp;
	
	@Column(name = "WITH_HOLDING_TAX_DC")
	private Double withHoldingTaxDc;
	
	@Column(name = "WITH_HOLDING_TAX_RCP")
	private Double withHoldingTaxRcp;
	
}
