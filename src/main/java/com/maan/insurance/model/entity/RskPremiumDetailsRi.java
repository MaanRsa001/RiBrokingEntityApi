package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
@IdClass(RskPremiumDetailsRiId.class)
@Table(name="RSK_PREMIUM_DETAILS_RI")
public class RskPremiumDetailsRi implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="TRANSACTION_NO", nullable=false)
	    private BigDecimal transactionNo ;

	    @Id
	    @Column(name="AMEND_ID", nullable=false)
	    private BigDecimal amendId ;

	    @Id
	    @Column(name="BRANCH_CODE", nullable=false, length=20)
	    private String     branchCode ;
	    
	    @Id
	    @Column(name="REINSURER_ID", nullable=false)
	    private String     reinsurerId ;
	    
	    @Id
	    @Column(name="BROKER_ID", nullable=false)
	    private String     brokerId ;


	    //--- ENTITY DATA FIELDS 
	    @Column(name="CONTRACT_NO")
	    private BigDecimal contractNo ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="TRANSACTION_MONTH_YEAR")
	    private Date       transactionMonthYear ;

	    @Column(name="ACCOUNT_PERIOD_QTR", length=10)
	    private String     accountPeriodQtr ;

	    @Column(name="ACCOUNT_PERIOD_YEAR")
	    private BigDecimal accountPeriodYear ;

	    @Column(name="CURRENCY_ID")
	    private BigDecimal currencyId ;

	    @Column(name="EXCHANGE_RATE")
	    private BigDecimal exchangeRate ;

	  
	    @Column(name="BROKERAGE_AMT_OC")
	    private BigDecimal brokerageAmtOc ;

	    @Column(name="BROKERAGE_AMT_DC")
	    private BigDecimal brokerageAmtDc ;

	    @Column(name="TAX")
	    private BigDecimal tax ;

	    @Column(name="TAX_AMT_OC")
	    private BigDecimal taxAmtOc ;

	    @Column(name="TAX_AMT_DC")
	    private BigDecimal taxAmtDc ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="ENTRY_DATE_TIME")
	    private Date       entryDateTime ;

	    @Column(name="COMMISSION")
	    private BigDecimal commission ;

	    @Column(name="PREMIUM_QUOTASHARE_OC")
	    private BigDecimal premiumQuotashareOc ;

	    @Column(name="PREMIUM_QUOTASHARE_DC")
	    private BigDecimal premiumQuotashareDc ;

	    @Column(name="COMMISSION_QUOTASHARE_OC")
	    private BigDecimal commissionQuotashareOc ;

	    @Column(name="COMMISSION_QUOTASHARE_DC")
	    private BigDecimal commissionQuotashareDc ;

	    @Column(name="PREMIUM_SURPLUS_OC")
	    private BigDecimal premiumSurplusOc ;

	    @Column(name="PREMIUM_SURPLUS_DC")
	    private BigDecimal premiumSurplusDc ;

	    @Column(name="COMMISSION_SURPLUS_OC")
	    private BigDecimal commissionSurplusOc ;

	    @Column(name="COMMISSION_SURPLUS_DC")
	    private BigDecimal commissionSurplusDc ;

	    @Column(name="PREMIUM_PORTFOLIOIN_OC")
	    private BigDecimal premiumPortfolioinOc ;

	    @Column(name="PREMIUM_PORTFOLIOIN_DC")
	    private BigDecimal premiumPortfolioinDc ;

	    @Column(name="CLAIM_PORTFOLIOIN_OC")
	    private BigDecimal claimPortfolioinOc ;

	    @Column(name="CLAIM_PORTFOLIOIN_DC")
	    private BigDecimal claimPortfolioinDc ;

	    @Column(name="PREMIUM_PORTFOLIOOUT_OC")
	    private BigDecimal premiumPortfoliooutOc ;

	    @Column(name="PREMIUM_PORTFOLIOOUT_DC")
	    private BigDecimal premiumPortfoliooutDc ;

	    @Column(name="LOSS_RESERVE_RELEASED_OC")
	    private BigDecimal lossReserveReleasedOc ;

	    @Column(name="LOSS_RESERVE_RELEASED_DC")
	    private BigDecimal lossReserveReleasedDc ;

	    @Column(name="PREMIUMRESERVE_QUOTASHARE_OC")
	    private BigDecimal premiumreserveQuotashareOc ;

	    @Column(name="PREMIUMRESERVE_QUOTASHARE_DC")
	    private BigDecimal premiumreserveQuotashareDc ;

	    @Column(name="CASH_LOSS_CREDIT_OC")
	    private BigDecimal cashLossCreditOc ;

	    @Column(name="CASH_LOSS_CREDIT_DC")
	    private BigDecimal cashLossCreditDc ;

	    @Column(name="LOSS_RESERVERETAINED_OC")
	    private BigDecimal lossReserveretainedOc ;

	    @Column(name="LOSS_RESERVERETAINED_DC")
	    private BigDecimal lossReserveretainedDc ;

	    @Column(name="PROFIT_COMMISSION_OC")
	    private BigDecimal profitCommissionOc ;

	    @Column(name="PROFIT_COMMISSION_DC")
	    private BigDecimal profitCommissionDc ;

	    @Column(name="CASH_LOSSPAID_OC")
	    private BigDecimal cashLosspaidOc ;

	    @Column(name="CASH_LOSSPAID_DC")
	    private BigDecimal cashLosspaidDc ;

	    @Column(name="STATUS", length=1)
	    private String     status ;

	    @Column(name="REMARKS", length=500)
	    private String     remarks ;

	    @Column(name="NETDUE_OC")
	    private BigDecimal netdueOc ;

	    @Column(name="NETDUE_DC")
	    private BigDecimal netdueDc ;

	    @Column(name="LAYER_NO")
	    private BigDecimal layerNo ;

	    @Column(name="M_DPREMIUM_OC")
	    private BigDecimal mDpremiumOc ;

	    @Column(name="M_DPREMIUM_DC")
	    private BigDecimal mDpremiumDc ;

	    @Column(name="ADJUSTMENT_PREMIUM_OC")
	    private BigDecimal adjustmentPremiumOc ;

	    @Column(name="ADJUSTMENT_PREMIUM_DC")
	    private BigDecimal adjustmentPremiumDc ;

	    @Column(name="REC_PREMIUM_OC")
	    private BigDecimal recPremiumOc ;

	    @Column(name="REC_PREMIUM_DC")
	    private BigDecimal recPremiumDc ;

	    @Column(name="ENTERING_MODE", length=5)
	    private String     enteringMode ;

	    @Column(name="RECEIPT_NO")
	    private BigDecimal receiptNo ;

	    @Column(name="CLAIMS_PAID_OC")
	    private BigDecimal claimsPaidOc ;

	    @Column(name="CLAIMS_PAID_DC")
	    private BigDecimal claimsPaidDc ;

	    @Column(name="SETTLEMENT_STATUS", length=20)
	    private String     settlementStatus ;

	    @Column(name="PAYMENT_NO")
	    private BigDecimal paymentNo ;

	    @Column(name="INSTALMENT_NUMBER", length=5)
	    private String     instalmentNumber ;

	    @Column(name="PREMIUM_RESERVE_RELEASE_OC")
	    private BigDecimal premiumReserveReleaseOc ;

	    @Column(name="PREMIUM_RESERVE_RELEASE_DC")
	    private BigDecimal premiumReserveReleaseDc ;

	    @Column(name="PREMIUM_RESERVE_RETAINED_OC")
	    private BigDecimal premiumReserveRetainedOc ;

	    @Column(name="PREMIUM_RESERVE_RETAINED_DC")
	    private BigDecimal premiumReserveRetainedDc ;

	    @Column(name="INTEREST_OC")
	    private BigDecimal interestOc ;

	    @Column(name="INTEREST_DC")
	    private BigDecimal interestDc ;

	    @Column(name="UW_MONTH")
	    private BigDecimal uwMonth ;

	    @Column(name="UW_YEAR")
	    private BigDecimal uwYear ;

	    @Column(name="XL_COST_OC")
	    private BigDecimal xlCostOc ;

	    @Column(name="XL_COST_DC")
	    private BigDecimal xlCostDc ;

	    @Column(name="CLAIM_PORTFOLIO_OUT_OC")
	    private BigDecimal claimPortfolioOutOc ;

	    @Column(name="CLAIM_PORTFOLIO_OUT_DC")
	    private BigDecimal claimPortfolioOutDc ;

	    @Column(name="PREMIUM_RESERVE_REALSED_OC")
	    private BigDecimal premiumReserveRealsedOc ;

	    @Column(name="PREMIUM_RESERVE_REALSED_DC")
	    private BigDecimal premiumReserveRealsedDc ;

	    @Column(name="OTHER_COST_OC")
	    private BigDecimal otherCostOc ;

	    @Column(name="OTHER_COST_DC")
	    private BigDecimal otherCostDc ;

	    @Column(name="ALLOCATED_TILL_DATE")
	    private BigDecimal allocatedTillDate ;

	    @Column(name="CEDANT_REFERENCE", length=100)
	    private String     cedantReference ;

	    @Column(name="ACC_PREMIUM", length=30)
	    private String     accPremium ;

	    @Column(name="CHECKYN", length=1)
	    private String     checkyn ;

	    @Column(name="TOTAL_CR_OC")
	    private BigDecimal totalCrOc ;

	    @Column(name="TOTAL_CR_DC")
	    private BigDecimal totalCrDc ;

	    @Column(name="TOTAL_DR_OC")
	    private BigDecimal totalDrOc ;

	    @Column(name="TOTAL_DR_DC")
	    private BigDecimal totalDrDc ;

	    @Column(name="OSCLAIM_LOSSUPDATE_OC")
	    private BigDecimal osclaimLossupdateOc ;

	    @Column(name="OSCLAIM_LOSSUPDATE_DC")
	    private BigDecimal osclaimLossupdateDc ;

	    @Column(name="PROPOSAL_NO")
	    private BigDecimal proposalNo ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="ENTRY_DATE")
	    private Date       entryDate ;

	    @Column(name="OVERRIDER")
	    private BigDecimal overrider ;

	    @Column(name="OVERRIDER_AMT_OC")
	    private BigDecimal overriderAmtOc ;

	    @Column(name="OVERRIDER_AMT_DC")
	    private BigDecimal overriderAmtDc ;

	    @Column(name="MOVEMENT_YN", length=1)
	    private String     movementYn ;

	    @Column(name="MOVMENT_TRANID")
	    private BigDecimal movmentTranid ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="AMENDMENT_DATE")
	    private Date       amendmentDate ;

	    @Column(name="WITH_HOLDING_TAX_DC")
	    private BigDecimal withHoldingTaxDc ;

	    @Column(name="WITH_HOLDING_TAX_OC")
	    private BigDecimal withHoldingTaxOc ;

	    @Column(name="RI_CESSION", length=10)
	    private String     riCession ;

	    @Column(name="SUB_CLASS")
	    private BigDecimal subClass ;

	    @Column(name="LOGIN_ID", length=100)
	    private String     loginId ;

	    @Column(name="TDS_OC")
	    private BigDecimal tdsOc ;

	    @Column(name="TDS_DC")
	    private BigDecimal tdsDc ;

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

	    @Column(name="SC_COMM_OC")
	    private BigDecimal scCommOc ;

	    @Column(name="SC_COMM_DC")
	    private BigDecimal scCommDc ;

	    @Column(name="BONUS_OC")
	    private BigDecimal bonusOc ;

	    @Column(name="BONUS_DC")
	    private BigDecimal bonusDc ;

	    @Column(name="PREMIUM_CLASS", length=100)
	    private String     premiumClass ;

	    @Column(name="PREMIUM_SUBCLASS", length=500)
	    private String     premiumSubclass ;

	    @Column(name="PRD_ALLOCATED_TILL_DATE")
	    private BigDecimal prdAllocatedTillDate ;

	    @Column(name="LRD_ALLOCATED_TILL_DATE")
	    private BigDecimal lrdAllocatedTillDate ;

	    @Column(name="GNPI_ENDT_NO", length=100)
	    private String     gnpiEndtNo ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="ACCOUNTING_PERIOD_DATE")
	    private Date       accountingPeriodDate ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="STATEMENT_DATE")
	    private Date       statementDate ;

	    @Column(name="OSBYN", length=10)
	    private String     osbyn ;

	    @Column(name="LPC_OC")
	    private BigDecimal lpcOc ;

	    @Column(name="LPC_DC")
	    private BigDecimal lpcDc ;

	    @Column(name="SECTION_NAME", length=500)
	    private String     sectionName ;

	    @Column(name="PRODUCT_ID")
	    private BigDecimal productId ;

	    @Column(name="REVERSE_TRANSACTION_NO")
	    private BigDecimal reverseTransactionNo ;

	    @Column(name="REVERSEL_STATUS", length=10)
	    private String     reverselStatus ;

	    @Column(name="REQUEST_NO")
	    private BigDecimal requestNo ;

	    @Column(name="TRANS_TYPE", length=10)
	    private String     transType ;

	    @Column(name="BUSINESS_TYPE", length=100)
	    private String     businessType ;
	    
	    @Column(name="CONTRACT_AMEND_ID")
	    private BigDecimal contractAmendId ;
	    
	    @Column(name="TRANS_STATUS")
	    private String     transStatus ;
	    
	    @Column(name="SIGN_SHARED")
	    private BigDecimal signShared ;
	    
	    @Column(name="BROKERAGE")
	    private BigDecimal brokerage ;

	    @Column(name="RI_TRANSACTION_NO")
	    private BigDecimal ritransactionNo ;
	    //--- ENTITY LINKS ( RELATIONSHIP )

	    @Column(name="OUR_BROKERAGE_OC")
	    private BigDecimal ourBrokerageOc ;
	    
	    @Column(name="OUR_BROKERAGE_DC")
	    private BigDecimal ourBrokerageDc ;
	    
	    @Column(name="OUR_BROKERAGE_VAT_OC")
	    private BigDecimal ourBrokerageVatOc ;
	    
	    
	    @Column(name="OUR_BROKERAGE_VAT_DC")
	    private BigDecimal ourBrokerageVatDc ;
	    
	    
	    @Column(name="PREMIUM_WHT_OC")
	    private BigDecimal premiumWhtOc ;
	    
	    
	    @Column(name="PREMIUM_WHT_DC")
	    private BigDecimal premiumWhtDc ;
	    
	    @Column(name="BROKERAGE_WHT_OC")
	    private BigDecimal brokerageWhtOc ;
	    
	    @Column(name="BROKERAGE_WHT_DC")
	    private BigDecimal brokerageWhtDc ;
	    
	}


