package com.maan.insurance.model.res.facPremium;

import java.util.List;

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
	@JsonProperty("Receiptno")
	private String receiptno;
	@JsonProperty("EnteringMode")
	private String enteringMode;
	@JsonProperty("OtherCost")
	private String otherCost;
	@JsonProperty("CedentRef")
	private String cedentRef;
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
	@JsonProperty("Bonus")
	private String bonus;
	@JsonProperty("TotalCredit")
	private String totalCredit;
	@JsonProperty("TotalDebit")
	private String totalDebit;
	@JsonProperty("Transaction")
	private String transaction;
	@JsonProperty("AccountPeriod")
	private String accountPeriod;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("AmendmentDate")
	private String amendmentDate; 
	@JsonProperty("Ricession")
	private String ricession;
	@JsonProperty("Commissionview")
	private String commissionview;
	@JsonProperty("Brokerageview")
	private String brokerageview;
	@JsonProperty("StatementDate")
	private String statementDate;
	@JsonProperty("Taxview")
	private String taxview;
	@JsonProperty("ChooseTransaction")
	private String chooseTransaction;
	@JsonProperty("TransDropDownVal")
	private String transDropDownVal;

	

}
