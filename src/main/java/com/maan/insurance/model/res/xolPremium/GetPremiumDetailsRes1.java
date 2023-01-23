package com.maan.insurance.model.res.xolPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.facPremium.SettlementstatusRes;

import lombok.Data;

@Data
public class GetPremiumDetailsRes1 {

//	@JsonProperty("Receiptno")
//	private String receiptno;
//	@JsonProperty("Status")
//	private String status;
	@JsonProperty("NetDue")
	private String netDue;
	@JsonProperty("Transaction")
	private String transaction;
//	@JsonProperty("PremiumQuotaShareusd")
//	private String premiumQuotaShareusd;
//	@JsonProperty("CommsissionQuotaShareusd")
//	private String commsissionQuotaShareusd;
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("ExchRate")
	private String exchRate;
//	@JsonProperty("PremiumQuotaShare")
//	private String premiumQuotaShare;
	@JsonProperty("Brokerageusd")
	private String brokerageusd;
	@JsonProperty("Taxusd")
	private String taxusd;
//	@JsonProperty("Commissionview")
//	private String commissionview;
//	@JsonProperty("CommissionQuotaShare")
//	private String 	commissionQuotaShare;
//	@JsonProperty("Brokerageview")
//	private String brokerageview;
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
//	@JsonProperty("Settlementstatus")
//	private String settlementstatus;

	@JsonProperty("TransactionNo")
	private String transactionNo;

	@JsonProperty("AmendmentDate")
	private String amendmentDate;
	@JsonProperty("Brokerage")
	private String brokerage;
//	@JsonProperty("Taxview")
//	private String taxview;
	@JsonProperty("Tax")
	private String tax;
	@JsonProperty("WithHoldingTaxOC")
	private String withHoldingTaxOC;
	@JsonProperty("Ricession")
	private String ricession;
	@JsonProperty("WithHoldingTaxDC")
	private String withHoldingTaxDC;
	@JsonProperty("DueDate")
	private String dueDate;
	@JsonProperty("Creditsign")
	private String Creditsign;
	@JsonProperty("TaxDedectSource")
	private String taxDedectSource;
	@JsonProperty("TaxDedectSourceDc")
	private String taxDedectSourceDc;
	@JsonProperty("VatPremium")
	private String vatPremium;
	@JsonProperty("VatPremiumDc")
	private String vatPremiumDc;
	@JsonProperty("BrokerageVat")
	private String brokerageVat;
	@JsonProperty("BrokerageVatDc")
	private String brokerageVatDc;
	@JsonProperty("DocumentType")
	private String documentType;
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
//	@JsonProperty("PremiumClass")
//	private String premiumClass;
//	@JsonProperty("PremiumSubClass")
//	private String premiumSubClass;
	@JsonProperty("ChooseTransaction")
	private String chooseTransaction;
	@JsonProperty("TransDropDownVal")
	private String transDropDownVal;
	@JsonProperty("StatementDate")
	private String statementDate;
//	@JsonProperty("Sumofpaidpremium")
//	private String 	sumofpaidpremium;
//	@JsonProperty("Epibalance")
//	private String epibalance;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("CurrencyName")
	private String 	currencyName;
	@JsonProperty("AccountPeriod")
	private String accountPeriod;
	@JsonProperty("SettlementstatusList")
	private List<SettlementstatusRes> SettlementstatusRes;

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
	@JsonProperty("ReinsuranceName")
	private String reinsuranceName;
	@JsonProperty("RiBroker")
	private String riBroker;
}

