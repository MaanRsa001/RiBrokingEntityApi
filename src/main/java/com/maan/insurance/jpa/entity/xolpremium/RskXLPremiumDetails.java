package com.maan.insurance.jpa.entity.xolpremium;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@DynamicInsert
@DynamicUpdate
@Builder

@Table(name="RSK_XL_PREMIUM_DETAILS")
public class RskXLPremiumDetails {

	@Id
	@Column(name = "CONTRACT_NO")
	private Long contractNo;

	@Column(name = "TRANSACTION_NO")
	private Integer transactionNo;

	@Column(name = "TRANSACTION_MONTH_YEAR")
	private Timestamp  transactionMonthYear;

	@Column(name = "ACCOUNT_PERIOD_QTR")
	private String accountPeriodQtr;

	@Column(name = "ACCOUNT_PERIOD_YEAR")
	private Double accountPeriodYear;

	@Column(name = "CURRENCY_ID")
	private Double currencyId;

	@Column(name = "EXCHANGE_RATE")
	private Double exchangeRate;

	@Column(name = "BROKERAGE")
	private Double brokerage;

	@Column(name = "BROKERAGE_AMT_OC")
	private Double brokerageAmtOc;

	@Column(name = "BROKERAGE_AMT_DC")
	private Double brokerageAmtDc;

	@Column(name = "TAX")
	private Double tax;

	@Column(name = "TAX_AMT_OC")
	private Double taxAmtOc;

	@Column(name = "TAX_AMT_DC")
	private Double taxAmtDc;

	@Column(name = "ENTRY_DATE_TIME")
	private Timestamp entryDateTime;

	@Column(name = "COMMISSION")
	private Double commission;

	@Column(name = "PREMIUM_QUOTASHARE_OC")
	private Double premiumQuotashareOc;

	@Column(name = "PREMIUM_QUOTASHARE_DC")
	private Double premiumQuotashareDc;

	@Column(name = "COMMISSION_QUOTASHARE_OC")
	private Double commissionQuotashareOc;

	@Column(name = "COMMISSION_QUOTASHARE_DC")
	private Double commissionQuotashareDc;

	@Column(name = "PREMIUM_SURPLUS_OC")
	private Double premiumSurplusOc;

	@Column(name = "PREMIUM_SURPLUS_DC")
	private Double premiumSurplusDc;

	@Column(name = "COMMISSION_SURPLUS_OC")
	private Double commissionSurplusOc;

	@Column(name = "COMMISSION_SURPLUS_DC")
	private Double commissionSurplusDc;

	@Column(name = "PREMIUM_PORTFOLIOIN_OC")
	private Double premiumPortfolioinOc;

	@Column(name = "PREMIUM_PORTFOLIOIN_DC")
	private Double premiumPortfolioinDc;

	@Column(name = "CLAIM_PORTFOLIOIN_OC")
	private Double claimPortfolioinOc;

	@Column(name = "CLAIM_PORTFOLIOIN_DC")
	private Double claimPortfolioinDc;

	@Column(name = "PREMIUM_PORTFOLIOOUT_OC")
	private Double premiumPortfoliooutOc;

	@Column(name = "PREMIUM_PORTFOLIOOUT_DC")
	private Double premiumPortfoliooutDc;

	@Column(name = "LOSS_RESERVE_RELEASED_OC")
	private Double lossReserveReleasedOc;

	@Column(name = "LOSS_RESERVE_RELEASED_DC")
	private Double lossReserveReleasedDc	;

	@Column(name = "PREMIUMRESERVE_QUOTASHARE_OC")
	private Double premiumreserveQuotashareOc;

	@Column(name = "PREMIUMRESERVE_QUOTASHARE_DC")
	private Double premiumreserveQuotashareDc;

	@Column(name = "CASH_LOSS_CREDIT_OC")
	private Double cashLossCreditOc;

	@Column(name = "CASH_LOSS_CREDIT_DC")
	private Double cashLossCreditDc;

	@Column(name = "LOSS_RESERVERETAINED_OC")
	private Double lossReserveretainedOc;

	@Column(name = "LOSS_RESERVERETAINED_DC")
	private Double lossReserveretainedDc	;

	@Column(name = "PROFIT_COMMISSION_OC")
	private Double profitCommissionOc;

	@Column(name = "PROFIT_COMMISSION_DC")
	private Double profitCommissionDc;

	@Column(name = "CASH_LOSSPAID_OC")
	private Double cashLosspaidOc;

	@Column(name = "CASH_LOSSPAID_DC")
	private Double cashLosspaidDc;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "NETDUE_OC")
	private Double netdueOc;

	@Column(name = "NETDUE_DC")
	private Double netdueDc;

	@Column(name = "LAYER_NO")
	private Integer layerNo;

	@Column(name = "M_DPREMIUM_OC")
	private Double mDpremiumOc;
	
	@Column(name = "M_DPREMIUM_DC")
	private Double mDpremiumDc;
	
	@Column(name = "ADJUSTMENT_PREMIUM_OC")
	private Double adjustmentPremiumOc;
	
	@Column(name = "ADJUSTMENT_PREMIUM_DC")
	private Double adjustmentPremiumDc;
	
	@Column(name = "REC_PREMIUM_OC")
	private Double recPremiumOc;
	
	@Column(name = "REC_PREMIUM_DC")
	private Double recPremiumDc;
	
	@Column(name = "ENTERING_MODE")
	private String enteringMode;
	
	@Column(name = "RECEIPT_NO")
	private Integer receiptNo;
	
	@Column(name = "CLAIMS_PAID_OC")
	private Double claimsPaidOc;
	
	@Column(name = "CLAIMS_PAID_DC")
	private Double claimsPaidDc;
	
	@Column(name = "SETTLEMENT_STATUS")
	private String settlementStatus;
	
	@Column(name = "PAYMENT_NO")
	private Integer paymentNo;
	
	@Column(name = "INSTALMENT_NUMBER")
	private String instalmentNumber;
	
	@Column(name = "PREMIUM_RESERVE_RELEASE_OC")
	private Double premiumReserveReleaseOc;
	
	@Column(name = "PREMIUM_RESERVE_RELEASE_DC")
	private Double premiumReserveReleaseDc;
	
	@Column(name = "PREMIUM_RESERVE_RETAINED_OC")
	private Double premiumReserveRetainedOc;
	
	@Column(name = "PREMIUM_RESERVE_RETAINED_DC")
	private Double premiumReserveRetainedDc;
	
	@Column(name = "INTEREST_OC")
	private Double interestOc;
	
	@Column(name = "INTEREST_DC")
	private Double interestDc;
	
	@Column(name = "UW_MONTH")
	private Double uwMonth;
	
	@Column(name = "UW_YEAR")
	private Double uwYear;
	
	@Column(name = "XL_COST_OC")
	private Double xlCostOc;
	
	@Column(name = "XL_COST_DC")
	private Double xlCostDc;
	
	@Column(name = "CLAIM_PORTFOLIO_OUT_OC")
	private Double claimPortfolioOutOc;
	
	@Column(name = "CLAIM_PORTFOLIO_OUT_DC")
	private Double claimPortfolioOutDc;
	
	@Column(name = "PREMIUM_RESERVE_REALSED_OC")
	private Double premiumReserveRealsedOc;
	
	@Column(name = "PREMIUM_RESERVE_REALSED_DC")
	private Double premiumReserveRealsedDc;
	
	@Column(name = "OTHER_COST_OC")
	private Double otherCostOc;
	
	@Column(name = "OTHER_COST_DC")
	private Double otherCostDc;
	
	@Column(name = "ALLOCATED_TILL_DATE")
	private Double allocatedTillDate;
	
	@Column(name = "CEDANT_REFERENCE")
	private String cedantReference;
	
	@Column(name = "ACC_PREMIUM")
	private String accPremium;
	
	@Column(name = "CHECKYN")
	private String checkyn;
	
	@Column(name = "TOTAL_CR_OC")
	private Double totalCrOc;
	
	@Column(name = "TOTAL_CR_DC")
	private Double totalCrDc;
	
	@Column(name = "TOTAL_DR_OC")
	private Double totalDrOc;

	@Column(name = "TOTAL_DR_DC")
	private Double totalDrDc;

	@Column(name = "OSCLAIM_LOSSUPDATE_OC")
	private Double osclaimLossupdateOc;

	@Column(name = "OSCLAIM_LOSSUPDATE_DC")
	private Double osclaimLossupdateDc;

	@Column(name = "PROPOSAL_NO")
	private Integer proposalNo;

	@Column(name = "ENTRY_DATE")
	private Timestamp entryDate;

	@Column(name = "OVERRIDER")
	private Double overrider;

	@Column(name = "OVERRIDER_AMT_OC")
	private Double overriderAmtOc;

	@Column(name = "OVERRIDER_AMT_DC")
	private Double overriderAmtDc;

	@Column(name = "MOVEMENT_YN")
	private String movementYn;

	@Column(name = "MOVMENT_TRANID")
	private Integer movmentTranid;

	@Column(name = "AMENDMENT_DATE")
	private Date amendmentDate;

	@Column(name = "WITH_HOLDING_TAX_DC")
	private Double withHoldingTaxDc;

	@Column(name = "WITH_HOLDING_TAX_OC")
	private Double withHoldingTaxOc;

	@Column(name = "AMEND_ID")
	private Integer amendId;

	@Column(name = "RI_CESSION")
	private String riCession;

	@Column(name = "SUB_CLASS")
	private Integer subClass;

	@Column(name = "LOGIN_ID")
	private String loginId;

	@Column(name = "BRANCH_CODE")
	private String branchCode;

	@Column(name = "TDS_OC")
	private Double tdsOc;

	@Column(name = "TDS_DC")
	private Double tdsDc;

	@Column(name = "ST_OC")
	private Double stOc;

	@Column(name = "ST_DC")
	private Double stDc;

	@Column(name = "SC_COMM_OC")
	private Double scCommOc;

	@Column(name = "SC_COMM_DC")
	private Double scCommDc;

	@Column(name = "BONUS_OC")
	private Double bonusOc;

	@Column(name = "BONUS_DC")
	private Double bonusDc;
	
	@Column(name = "GNPI_ENDT_NO")
	private String gnpiEndtNo;

	@Column(name = "PREMIUM_CLASS")
	private String premiumClass;

	@Column(name = "PREMIUM_SUBCLASS")
	private String premiumSubclass;
	
	@Column(name = "STATEMENT_DATE")
	private Date statementDate;
	
	@Column(name = "PRODUCT_ID")
	private Integer  productId;
	
	@Column(name = "REVERSEL_STATUS")
	private String reverselStatus;
	
	@Column(name = "REVERSE_TRANSACTION_NO")
	private Integer reverseTransactionNo;
	
}
