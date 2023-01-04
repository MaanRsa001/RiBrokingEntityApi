package com.maan.insurance.model.entity;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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

@Table(name = "RSK_PREMIUM_DETAILS_TEMP")
public class RskPremiumDetailsTemp {

	@Id
	@Column(name = "REQUEST_NO")
	private BigDecimal requestNo;

	@Column(name = "CONTRACT_NO")
	private BigDecimal contractNo;

	@Column(name = "TRANSACTION_NO")
	private BigDecimal transactionNo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TRANSACTION_MONTH_YEAR")
	private Date transactionMonthYear;

	@Column(name = "ACCOUNT_PERIOD_QTR")
	private String accountPeriodQtr;

	@Column(name = "ACCOUNT_PERIOD_YEAR")
	private BigDecimal accountPeriodYear;

	@Column(name = "CURRENCY_ID")
	private BigDecimal currencyId;

	@Column(name = "EXCHANGE_RATE")
	private BigDecimal exchangeRate;

	@Column(name = "BROKERAGE")
	private BigDecimal brokerage;

	@Column(name = "BROKERAGE_AMT_OC")
	private BigDecimal brokerageAmtOc;

	@Column(name = "BROKERAGE_AMT_DC")
	private BigDecimal brokerageAmtDc;

	@Column(name = "TAX")
	private BigDecimal tax;

	@Column(name = "TAX_AMT_OC")
	private BigDecimal taxAmtOc;

	@Column(name = "TAX_AMT_DC")
	private BigDecimal taxAmtDc;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE_TIME")
	private Date entryDateTime;

	@Column(name = "COMMISSION")
	private BigDecimal commission;

	@Column(name = "PREMIUM_QUOTASHARE_OC")
	private BigDecimal premiumQuotashareOc;

	@Column(name = "PREMIUM_QUOTASHARE_DC")
	private BigDecimal premiumQuotashareDc;

	@Column(name = "COMMISSION_QUOTASHARE_OC")
	private BigDecimal commissionQuotashareOc;

	@Column(name = "COMMISSION_QUOTASHARE_DC")
	private BigDecimal commissionQuotashareDc;

	@Column(name = "PREMIUM_SURPLUS_OC")
	private BigDecimal premiumSurplusOc;

	@Column(name = "PREMIUM_SURPLUS_DC")
	private BigDecimal premiumSurplusDc;

	@Column(name = "COMMISSION_SURPLUS_OC")
	private BigDecimal commissionSurplusOc;

	@Column(name = "COMMISSION_SURPLUS_DC")
	private BigDecimal commissionSurplusDc;

	@Column(name = "PREMIUM_PORTFOLIOIN_OC")
	private BigDecimal premiumPortfolioinOc;

	@Column(name = "PREMIUM_PORTFOLIOIN_DC")
	private BigDecimal premiumPortfolioinDc;

	@Column(name = "CLAIM_PORTFOLIOIN_OC")
	private BigDecimal claimPortfolioinOc;

	@Column(name = "CLAIM_PORTFOLIOIN_DC")
	private BigDecimal claimPortfolioinDc;

	@Column(name = "PREMIUM_PORTFOLIOOUT_OC")
	private BigDecimal premiumPortfoliooutOc;

	@Column(name = "PREMIUM_PORTFOLIOOUT_DC")
	private BigDecimal premiumPortfoliooutDc;

	@Column(name = "LOSS_RESERVE_RELEASED_OC")
	private BigDecimal lossReserveReleasedOc;

	@Column(name = "LOSS_RESERVE_RELEASED_DC")
	private BigDecimal lossReserveReleasedDc;

	@Column(name = "PREMIUMRESERVE_QUOTASHARE_OC")
	private BigDecimal premiumreserveQuotashareOc;

	@Column(name = "PREMIUMRESERVE_QUOTASHARE_DC")
	private BigDecimal premiumreserveQuotashareDc;

	@Column(name = "CASH_LOSS_CREDIT_OC")
	private BigDecimal cashLossCreditOc;

	@Column(name = "CASH_LOSS_CREDIT_DC")
	private BigDecimal cashLossCreditDc;

	@Column(name = "LOSS_RESERVERETAINED_OC")
	private BigDecimal lossReserveretainedOc;

	@Column(name = "LOSS_RESERVERETAINED_DC")
	private BigDecimal lossReserveretainedDc;

	@Column(name = "PROFIT_COMMISSION_OC")
	private BigDecimal profitCommissionOc;

	@Column(name = "PROFIT_COMMISSION_DC")
	private BigDecimal profitCommissionDc;

	@Column(name = "CASH_LOSSPAID_OC")
	private BigDecimal cashLosspaidOc;

	@Column(name = "CASH_LOSSPAID_DC")
	private BigDecimal cashLosspaidDc;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "NETDUE_OC")
	private BigDecimal netdueOc;

	@Column(name = "NETDUE_DC")
	private BigDecimal netdueDc;

	@Column(name = "LAYER_NO")
	private BigDecimal layerNo;

	@Column(name = "M_DPREMIUM_OC")
	private BigDecimal mDpremiumOc;

	@Column(name = "M_DPREMIUM_DC")
	private BigDecimal mDpremiumDc;

	@Column(name = "ADJUSTMENT_PREMIUM_OC")
	private BigDecimal adjustmentPremiumOc;

	@Column(name = "ADJUSTMENT_PREMIUM_DC")
	private BigDecimal adjustmentPremiumDc;

	@Column(name = "REC_PREMIUM_OC")
	private BigDecimal recPremiumOc;

	@Column(name = "REC_PREMIUM_DC")
	private BigDecimal recPremiumDc;

	@Column(name = "ENTERING_MODE")
	private String enteringMode;

	@Column(name = "RECEIPT_NO")
	private BigDecimal receiptNo;

	@Column(name = "CLAIMS_PAID_OC")
	private BigDecimal claimsPaidOc;

	@Column(name = "CLAIMS_PAID_DC")
	private BigDecimal claimsPaidDc;

	@Column(name = "SETTLEMENT_STATUS")
	private String settlementStatus;

	@Column(name = "PAYMENT_NO")
	private BigDecimal paymentNo;

	@Column(name = "INSTALMENT_NUMBER")
	private String instalmentNumber;

	@Column(name = "PREMIUM_RESERVE_RELEASE_OC")
	private BigDecimal premiumReserveReleaseOc;

	@Column(name = "PREMIUM_RESERVE_RELEASE_DC")
	private BigDecimal premiumReserveReleaseDc;

	@Column(name = "PREMIUM_RESERVE_RETAINED_OC")
	private BigDecimal premiumReserveRetainedOc;

	@Column(name = "PREMIUM_RESERVE_RETAINED_DC")
	private BigDecimal premiumReserveRetainedDc;

	@Column(name = "INTEREST_OC")
	private BigDecimal interestOc;

	@Column(name = "INTEREST_DC")
	private BigDecimal interestDc;

	@Column(name = "UW_MONTH")
	private BigDecimal uwMonth;

	@Column(name = "UW_YEAR")
	private BigDecimal uwYear;

	@Column(name = "XL_COST_OC")
	private BigDecimal xlCostOc;

	@Column(name = "XL_COST_DC")
	private BigDecimal xlCostDc;

	@Column(name = "CLAIM_PORTFOLIO_OUT_OC")
	private BigDecimal claimPortfolioOutOc;

	@Column(name = "CLAIM_PORTFOLIO_OUT_DC")
	private BigDecimal claimPortfolioOutDc;

	@Column(name = "PREMIUM_RESERVE_REALSED_OC")
	private BigDecimal premiumReserveRealsedOc;

	@Column(name = "PREMIUM_RESERVE_REALSED_DC")
	private BigDecimal premiumReserveRealsedDc;

	@Column(name = "OTHER_COST_OC")
	private BigDecimal otherCostOc;

	@Column(name = "OTHER_COST_DC")
	private BigDecimal otherCostDc;

	@Column(name = "ALLOCATED_TILL_DATE")
	private BigDecimal allocatedTillDate;

	@Column(name = "CEDANT_REFERENCE")
	private String cedantReference;

	@Column(name = "ACC_PREMIUM")
	private String accPremium;

	@Column(name = "CHECKYN")
	private String checkyn;

	@Column(name = "TOTAL_CR_OC")
	private BigDecimal totalCrOc;

	@Column(name = "TOTAL_CR_DC")
	private BigDecimal totalCrDc;

	@Column(name = "TOTAL_DR_OC")
	private BigDecimal totalDrOc;

	@Column(name = "TOTAL_DR_DC")
	private BigDecimal totalDrDc;

	@Column(name = "OSCLAIM_LOSSUPDATE_OC")
	private BigDecimal osclaimLossupdateOc;

	@Column(name = "OSCLAIM_LOSSUPDATE_DC")
	private BigDecimal osclaimLossupdateDc;

	@Column(name = "PROPOSAL_NO")
	private BigDecimal proposalNo;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE")
	private Date entryDate;

	@Column(name = "OVERRIDER")
	private BigDecimal overrider;

	@Column(name = "OVERRIDER_AMT_OC")
	private BigDecimal overriderAmtOc;

	@Column(name = "OVERRIDER_AMT_DC")
	private BigDecimal overriderAmtDc;

	@Column(name = "MOVEMENT_YN")
	private String movementYn;

	@Column(name = "MOVMENT_TRANID")
	private BigDecimal movmentTranid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AMENDMENT_DATE")
	private Date amendmentDate;

	@Column(name = "WITH_HOLDING_TAX_DC")
	private BigDecimal withHoldingTaxDc;

	@Column(name = "WITH_HOLDING_TAX_OC")
	private BigDecimal withHoldingTaxOc;

	@Column(name = "AMEND_ID")
	private BigDecimal amendId;

	@Column(name = "RI_CESSION")
	private String riCession;

	@Column(name = "SUB_CLASS")
	private BigDecimal subClass;

	@Column(name = "LOGIN_ID")
	private String loginId;

	@Column(name = "BRANCH_CODE")
	private String branchCode;

	@Column(name = "TDS_OC")
	private BigDecimal tdsOc;

	@Column(name = "TDS_DC")
	private BigDecimal tdsDc;

	@Column(name="VAT_PREMIUM_OC")
    private BigDecimal vatPremiumOc ;

    @Column(name="VAT_PREMIUM_DC")
    private BigDecimal vatPremiumDc ;
    
    @Column(name="BROKERAGE_VAT_OC")
    private BigDecimal brokerageVatOc ;

    @Column(name="BROKERAGE_VAT_DC")
    private BigDecimal brokerageVatDc ;
    
    @Column(name = "DOCUMENT_TYPE")
	private String documentType;

	@Column(name = "SC_COMM_OC")
	private BigDecimal scCommOc;

	@Column(name = "SC_COMM_DC")
	private BigDecimal scCommDc;

	@Column(name = "BONUS_OC")
	private BigDecimal bonusOc;

	@Column(name = "BONUS_DC")
	private BigDecimal bonusDc;

	@Column(name = "PREMIUM_CLASS")
	private String premiumClass;

	@Column(name = "PREMIUM_SUBCLASS")
	private String premiumSubclass;

	@Column(name = "PRD_ALLOCATED_TILL_DATE")
	private BigDecimal prdAllocatedTillDate;

	@Column(name = "LRD_ALLOCATED_TILL_DATE")
	private BigDecimal lrdAllocatedTillDate;

	@Column(name = "GNPI_ENDT_NO")
	private String gnpiEndtNo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACCOUNTING_PERIOD_DATE")
	private Date accountingPeriodDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STATEMENT_DATE")
	private Date statementDate;

	@Column(name = "OSBYN")
	private String osbyn;

	@Column(name = "LPC_OC")
	private BigDecimal lpcOc;

	@Column(name = "LPC_DC")
	private BigDecimal lpcDc;

	@Column(name = "SECTION_NAME")
	private String sectionName;

	@Column(name = "PRODUCT_ID")
	private BigDecimal productId;

	@Column(name = "REVERSE_TRANSACTION_NO")
	private BigDecimal reverseTransactionNo;

	@Column(name = "REVERSEL_STATUS")
	private String reverselStatus;

	@Column(name = "TRANS_STATUS")
	private String transStatus;

	@Column(name = "TRANS_TYPE")
	private String transType;

	@Column(name = "REVIEWER_ID")
	private String reviewerId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REVIEW_DATE")
	private Date reviewDate;

	@Column(name = "M1_OC")
	private BigDecimal m1Oc;

	@Column(name = "M2_OC")
	private BigDecimal m2Oc;

	@Column(name = "M3_OC")
	private BigDecimal m3Oc;

}
