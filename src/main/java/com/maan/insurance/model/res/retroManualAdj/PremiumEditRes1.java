package com.maan.insurance.model.res.retroManualAdj;

import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.Data;

@Data
public class PremiumEditRes1 {

	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("ExchRate")
	private String exchRate;
	@JsonProperty("PremiumQuotaShare")
	private String premiumQuotaShare;
	@JsonProperty("CommissionQuotaShare")
	private String commissionQuotaShare;
	@JsonProperty("Brokerage")
	private String brokerage;
	@JsonProperty("Tax")
	private String tax;
	@JsonProperty("Status")
	private String status; 
//	@JsonProperty("Receiptno")
//	private String receiptno;
//	@JsonProperty("EnteringMode")
//	private String enteringMode;
	@JsonProperty("OtherCost")
	private String otherCost;
//	@JsonProperty("CedentRef")
//	private String cedentRef;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("NetDue")
	private String netDue;
	@JsonProperty("WithHoldingTaxOC")
	private String withHoldingTaxOC;
	@JsonProperty("TaxDedectSource")
	private String taxDedectSource;
	@JsonProperty("ServiceTax")
	private String serviceTax;
//	@JsonProperty("Bonus")
//	private String bonus;
//	@JsonProperty("TotalCredit")
//	private String totalCredit;
//	@JsonProperty("TotalDebit")
//	private String totalDebit;
	@JsonProperty("Transaction")
	private String transaction;
//	@JsonProperty("AccountPeriod")
//	private String accountPeriod;
//	@JsonProperty("InceptionDate")
//	private String inceptionDate;
	@JsonProperty("AmendmentDate")
	private String amendmentDate; 
//	@JsonProperty("Ricession")
//	private String ricession;
//	@JsonProperty("Commissionview")
//	private String commissionview;
//	@JsonProperty("Brokerageview")
//	private String brokerageview;
//	@JsonProperty("StatementDate")
//	private String statementDate;
//	@JsonProperty("Taxview")
//	private String taxview;
//	@JsonProperty("ChooseTransaction")
//	private String chooseTransaction;
//	@JsonProperty("TransDropDownVal")
//	private String transDropDownVal;

	
	@JsonProperty("PremiumSurplus")
	private String premiumSurplus; 
	@JsonProperty("CommissionSurplus")
	private String commissionSurplus;
	@JsonProperty("PremiumportifolioIn")
	private String premiumportifolioIn;
	@JsonProperty("CliamPortfolioin")
	private String cliamPortfolioin;
	@JsonProperty("Premiumportifolioout")
	private String premiumportifolioout;
	@JsonProperty("LossReserveReleased")
	private String lossReserveReleased;
	@JsonProperty("PremiumReserveQuotaShare")
	private String premiumReserveQuotaShare;
	@JsonProperty("CashLossCredit")
	private String cashLossCredit;
	@JsonProperty("LossReserveRetained")
	private String lossReserveRetained; 
	@JsonProperty("ProfitCommission")
	private String profitCommission;
	@JsonProperty("CashLossPaid")
	private String cashLossPaid;
	@JsonProperty("Claimspaid")
	private String claimspaid;
	@JsonProperty("Mdpremium")
	private String mdpremium;
	@JsonProperty("Adjustmentpremium")
	private String adjustmentpremium;
	@JsonProperty("Recuirementpremium")
	private String recuirementpremium;
//	@JsonProperty("Commission")
//	private String commission;
	@JsonProperty("XlCost")
	private String xlCost; 
	@JsonProperty("Cliamportfolioout")
	private String cliamportfolioout;
	@JsonProperty("PremiumReserveReleased")
	private String premiumReserveReleased;
	@JsonProperty("Interest")
	private String interest;
	@JsonProperty("OsClaimsLossUpdateOC")
	private String OsClaimsLossUpdateOC;
	@JsonProperty("Overrider")
	private String overrider;
	@JsonProperty("OverriderUSD")
	private String overriderUSD;
	@JsonProperty("Predepartment")
	private String predepartment;
//	@JsonProperty("DepartmentId")
//	private String departmentId; 
//	@JsonProperty("AccountPeriodyear")
//	private String accountPeriodyear;
//	@JsonProperty("InstlmentNo")
//	private String instlmentNo;
	@JsonProperty("WithHoldingTaxDC")
	private String withHoldingTaxDC;
	@JsonProperty("TaxDedectSourceDc")
	private String taxDedectSourceDc;
	@JsonProperty("ServiceTaxDc")
	private String serviceTaxDc;
//	@JsonProperty("BonusDc")
//	private String bonusDc;
//	@JsonProperty("GnpiDate")
//	private String gnpiDate;
//	@JsonProperty("ContNo")
//	private String contNo;
//	@JsonProperty("TransactionNo")
//	private String transactionNo;
//	@JsonProperty("Layerno")
//	private String layerno;
//	@JsonProperty("Brokerageusd")
//	private String brokerageusd;
//	@JsonProperty("Taxusd")
//	private String taxusd;
//	@JsonProperty("Mdpremiumusd")
//	private String mdpremiumusd;
//	@JsonProperty("Adjustmentpremiumusd")
//	private String adjustmentpremiumusd;
//	@JsonProperty("Recuirementpremiumusd")
//	private String recuirementpremiumusd;
	@JsonProperty("Netdueusd")
	private String netdueusd;
//	@JsonProperty("OtherCostUSD")
//	private String otherCostUSD;
//	@JsonProperty("TotalCreditDC")
//	private String totalCreditDC;
//	@JsonProperty("TotalDebitDC")
//	private String totalDebitDC;
//	@JsonProperty("DueDate")
//	private String dueDate;
//	@JsonProperty("Creditsign")
//	private String creditsign;

	@JsonProperty("LossParticipation")
	private String lossParticipation;
	@JsonProperty("LossParticipationDC")
	private String lossParticipationDC;
	@JsonProperty("SlideScaleCom")
	private String slideScaleCom;
	@JsonProperty("SlideScaleComDC")
	private String slideScaleComDC;
	@JsonProperty("SubProfitId")
	private String subProfitId;
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("Reference")
	private String reference;
	@JsonProperty("BusinessType")
	private String businessType;


}
