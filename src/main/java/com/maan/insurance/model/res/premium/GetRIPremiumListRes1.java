package com.maan.insurance.model.res.premium;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetRIPremiumListRes1 {
	@JsonProperty("ReInsurerName")
	private String reInsurerName;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("BrokerId")
	private String brokerId;
//	@JsonProperty("Brokerage")
//	private String brokerage;
	@JsonProperty("SignShared")
	private String signShared;
	@JsonProperty("ReinsurerId")
	private String reinsurerId;

	@JsonProperty("VatPremiumOc")
	private String vatPremiumOc;
	
	@JsonProperty("VatPremiumDc")
	private String vatPremiumDc;

	

	
	


	@JsonProperty("NetDue")
	private String netDue;
	@JsonProperty("Transaction")
	private String transaction;

	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("ExchRate")
	private String exchRate;

	@JsonProperty("Brokerageusd")
	private String brokerageusd;
	@JsonProperty("TaxOc")
	private String taxOc;
	@JsonProperty("Taxusd")
	private String taxusd;

	@JsonProperty("Netdueusd")
	private String netdueusd;
	@JsonProperty("OtherCostUSD")
	private String otherCostUSD;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("TotalCredit")
	private String totalCredit;
	@JsonProperty("TotalDebit")
	private String totalDebit;
	@JsonProperty("TotalDebitDC")
	private String totalDebitDC;


	@JsonProperty("TransactionNo")
	private String transactionNo;

	@JsonProperty("AmendmentDate")
	private String amendmentDate;
	@JsonProperty("Brokerage")
	private String brokerage;

	@JsonProperty("Tax")
	private String tax;
	@JsonProperty("WithHoldingTaxOC")
	private String withHoldingTaxOC;
	@JsonProperty("Ricession")
	private String ricession;
	@JsonProperty("WithHoldingTaxDC")
	private String withHoldingTaxDC;
//	@JsonProperty("DueDate")
//	private String dueDate;
	@JsonProperty("Creditsign")
	private String Creditsign;
	@JsonProperty("TaxDedectSource")
	private String taxDedectSource;
	@JsonProperty("TaxDedectSourceDc")
	private String taxDedectSourceDc;

	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("Bonus")
	private String 	bonus;
	@JsonProperty("OtherCost")
	private String otherCost;
	@JsonProperty("CedentRef")
	private String cedentRef; 
	@JsonProperty("TotalCreditDC")
	private String totalCreditDC;
	@JsonProperty("BonusDc")
	private String bonusDc;

	@JsonProperty("ChooseTransaction")
	private String chooseTransaction;
	@JsonProperty("TransDropDownVal")
	private String transDropDownVal;
	@JsonProperty("StatementDate")
	private String statementDate;

//	@JsonProperty("Currency")
//	private String currency;
//	@JsonProperty("CurrencyName")
//	private String 	currencyName;
	@JsonProperty("AccountPeriod")
	private String accountPeriod;
//	@JsonProperty("SettlementstatusList")
//	private List<SettlementstatusRes> SettlementstatusRes;

	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("Mdpremium")
	private String mdpremium;
	@JsonProperty("Adjustmentpremium")
	private String adjustmentpremium;
	@JsonProperty("Recuirementpremium")
	private String recuirementpremium;
	@JsonProperty("Layerno")
	private String layerno;
	@JsonProperty("EnteringMode")
	private String enteringMode;
	@JsonProperty("Mdpremiumusd")
	private String mdpremiumusd;
	@JsonProperty("Adjustmentpremiumusd")
	private String adjustmentpremiumusd;
	@JsonProperty("Recuirementpremiumusd")
	private String recuirementpremiumusd;
	@JsonProperty("Predepartment")
	private String predepartment;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("GnpiDate")
	private String gnpiDate;
	@JsonProperty("BrokerageVatOc")
	private String brokerageVatOc;
	
	@JsonProperty("BrokerageVatDc")
	private String brokerageVatDc;
	@JsonProperty("DocumentType")
	private String documentType;
}
