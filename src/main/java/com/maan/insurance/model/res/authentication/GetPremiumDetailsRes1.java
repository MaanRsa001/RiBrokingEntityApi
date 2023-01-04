package com.maan.insurance.model.res.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPremiumDetailsRes1 {
	@JsonProperty("ContNo")
	private String contNo;

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
	@JsonProperty("NetDue")
	private String netDue;
	@JsonProperty("Receiptno")
	private String receiptno;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("AccountPeriod")
	private String accountPeriod;

	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("Currency")
	private String currency;

	@JsonProperty("OtherCost")
	private String otherCost;

	@JsonProperty("CedentRef")
	private String cedentRef;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("TotalCredit")
	private String totalCredit;

	@JsonProperty("TotalDebit")
	private String totalDebit;

	@JsonProperty("AmendmentDate")
	private String amendmentDate;
	@JsonProperty("WithHoldingTaxOC")
	private String withHoldingTaxOC;

	@JsonProperty("Creditsign")
	private String creditsign;
	@JsonProperty("RiCession")
	private String riCession;
	@JsonProperty("TaxDedectSource")
	private String taxDedectSource;

	@JsonProperty("ServiceTax")
	private String serviceTax;

	@JsonProperty("ExchRate")
	private String exchRate;
	@JsonProperty("StatementDate")
	private String statementDate;

	
	@JsonProperty("Commissionview")
	private String commissionview;
	@JsonProperty("Brokerageview")
	private String brokerageview;
	@JsonProperty("Taxview")
	private String taxview;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("EnteringMode")
	private String enteringMode;
	@JsonProperty("Bonus")
	private String bonus;

	@JsonProperty("ChooseTransaction")
	private String chooseTransaction;
	@JsonProperty("TransDropDownVal")
	private String transDropDownVal;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("DepartmentId")
	private String departmentId;
}
