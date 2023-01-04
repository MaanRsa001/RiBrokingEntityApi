package com.maan.insurance.model.res.premium;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.GetCommonValueRes;

import lombok.Data;

@Data
public class GetPremiumDetailsRes1 {
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("Transaction")
	private String transaction;
	@JsonProperty("Brokerage")
	private String brokerage;
	@JsonProperty("Tax")
	private String tax;
	@JsonProperty("PremiumQuotaShare")
	private String premiumQuotaShare;
	@JsonProperty("CommissionQuotaShare")
	private String commissionQuotaShare;
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
	@JsonProperty("NetDue")
	private String netDue;
	@JsonProperty("Receiptno")
	private String receiptno;
	@JsonProperty("ClaimsPaid")
	private String claimsPaid;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("XlCost")
	private String xlCost;
	@JsonProperty("CliamPortfolioOut")
	private String cliamPortfolioOut;
	@JsonProperty("PremiumReserveReleased")
	private String premiumReserveReleased;
	@JsonProperty("AccountPeriod")
	private String accountPeriod;
	@JsonProperty("AccountPeriodYear")
	private String accountPeriodYear;
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("BrokerageUsd")
	private String brokerageUsd;
	@JsonProperty("OtherCost")
	private String otherCost;
	@JsonProperty("TaxUsd")
	private String taxUsd;
	@JsonProperty("PremiumQuotaShareUsd")
	private String premiumQuotaShareUsd;
	@JsonProperty("CommsissionQuotaShareUsd")
	private String commsissionQuotaShareUsd;
	@JsonProperty("PremiumSurplusUsd")
	private String premiumSurplusUsd;
	@JsonProperty("ComissionSurplusUsd")
	private String comissionSurplusUsd;
	@JsonProperty("PremiumPortfolioInUsd")
	private String premiumPortfolioInUsd;
	
	@JsonProperty("CliamPortfolioUsd")
	private String cliamPortfolioUsd;
	@JsonProperty("PremiumPortfolioOutUsd")
	private String premiumPortfolioOutUsd;
	@JsonProperty("LossReserveReleasedUsd")
	private String lossReserveReleasedUsd;
	@JsonProperty("PremiumReserveQuotaShareUsd")
	private String premiumReserveQuotaShareUsd;
	@JsonProperty("CashLossCreditUsd")
	private String cashLossCreditUsd;
	@JsonProperty("LossReserveRetainedUsd")
	private String lossReserveRetainedUsd;
	@JsonProperty("ProfitCommissionUsd")
	private String profitCommissionUsd;
	@JsonProperty("CashLossPaidUsd")
	private String cashLossPaidUsd;
	@JsonProperty("ClamsPaidUsd")
	private String clamsPaidUsd;
	@JsonProperty("XlCostUsd")
	private String xlCostUsd;
	@JsonProperty("CliamPortfolioOutUsd")
	private String cliamPortfolioOutUsd;
	@JsonProperty("PremiumReserveReleasedUsd")
	private String premiumReserveReleasedUsd;
	@JsonProperty("NetDueUsd")
	private String netDueUsd;
	@JsonProperty("OtherCostUSD")
	private String otherCostUSD;
	@JsonProperty("CedentRef")
	private String cedentRef;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("TotalCredit")
	private String totalCredit;
	@JsonProperty("TotalCreditDC")
	private String totalCreditDC;
	@JsonProperty("TotalDebit")
	private String totalDebit;
	@JsonProperty("TotalDebitDC")
	private String totalDebitDC;
	@JsonProperty("Interest")
	private String interest;
	@JsonProperty("InterestDC")
	private String interestDC;
	@JsonProperty("OsClaimsLossUpdateOC")
	private String osClaimsLossUpdateOC;
	@JsonProperty("OsClaimsLossUpdateDC")
	private String osClaimsLossUpdateDC;
	@JsonProperty("Overrider")
	private String overrider;
	@JsonProperty("OverriderUSD")
	private String overriderUSD;
	@JsonProperty("AmendmentDate")
	private String amendmentDate;
	@JsonProperty("WithHoldingTaxOC")
	private String withHoldingTaxOC;
	@JsonProperty("WithHoldingTaxDC")
	private String withHoldingTaxDC;
	@JsonProperty("DueDate")
	private String dueDate;
	@JsonProperty("Creditsign")
	private String creditsign;
	@JsonProperty("RiCession")
	private String riCession;
	@JsonProperty("TaxDedectSource")
	private String taxDedectSource;
	@JsonProperty("TaxDedectSourceDc")
	private String taxDedectSourceDc;
	@JsonProperty("ServiceTax")
	private String serviceTax;
	@JsonProperty("ServiceTaxDc")
	private String serviceTaxDc;
	
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
	@JsonProperty("ExchRate")
	private String exchRate;
	@JsonProperty("StatementDate")
	private String statementDate;
	@JsonProperty("PremiumClass")
	private String premiumClass;
	@JsonProperty("PremiumSubClass")
	private String premiumSubClass;
	@JsonProperty("OsbYN")
	private String osbYN;
	@JsonProperty("SectionName")
	private String sectionName;
	@JsonProperty("AccDate")
	private String accDate;
	@JsonProperty("SumOfPaidPremium")
	private String sumOfPaidPremium;
	@JsonProperty("CurrencyName")
	private String currencyName;
	
	
}
