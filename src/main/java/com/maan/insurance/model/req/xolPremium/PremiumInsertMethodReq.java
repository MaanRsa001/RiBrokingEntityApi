package com.maan.insurance.model.req.xolPremium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiumInsertMethodReq {

	@JsonProperty("Receiptno")
	private String receiptno;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("Transaction")
	private String transaction;
	@JsonProperty("Instalmentdate")
	private String instalmentdate;
	@JsonProperty("InstlmentNo")
	private String instlmentNo;
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("ExchRate")
	private String exchRate;
//	@JsonProperty("PremiumQuotaShare")
//	private String premiumQuotaShare;
	@JsonProperty("EnteringMode")
	private String enteringMode;
	@JsonProperty("ShareSigned")
	private String shareSigned;
//	@JsonProperty("Commissionview")
//	private String commissionview;
//	@JsonProperty("CommissionQuotaShare")
//	private String 	commissionQuotaShare;
	@JsonProperty("Brokerageview")
	private String brokerageview;
//	@JsonProperty("Status")
//	private String status;
	@JsonProperty("TransDropDownVal")
	private String transDropDownVal;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("TotalCredit")
	private String totalCredit;
	@JsonProperty("TotalDebit")
	private String totalDebit;
	@JsonProperty("StatementDate")
	private String statementDate;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ChooseTransaction")
	private String chooseTransaction;
	@JsonProperty("ButtonStatus")
	private String buttonStatus;
	@JsonProperty("Mode")
	private String mode;
	@JsonProperty("RequestNo")
	private String requestNo;
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("TableType")
	private String tableType;
	@JsonProperty("Layerno")
	private String layerno;
	@JsonProperty("AmendmentDate")
	private String amendmentDate;
	@JsonProperty("Brokerage")
	private String brokerage;
	@JsonProperty("Taxview")
	private String taxview;
	@JsonProperty("Tax")
	private String tax;
	@JsonProperty("WithHoldingTaxOC")
	private String withHoldingTaxOC;
	@JsonProperty("Ricession")
	private String ricession;
	
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("TaxDedectSource")
	private String taxDedectSource;
	@JsonProperty("ServiceTax")
	private String serviceTax;
	@JsonProperty("Bonus")
	private String bonus;
	@JsonProperty("Predepartment")
	private String predepartment;
	@JsonProperty("SubProfitId")
	private String subProfitId;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("Settlementstatus")
	private String 	settlementstatus;
	@JsonProperty("OtherCost")
	private String otherCost;
	@JsonProperty("CedentRef")
	private String cedentRef; 
	@JsonProperty("SpRetro")
	private String spRetro;
	@JsonProperty("NoOfRetro")
	private String noOfRetro;
	@JsonProperty("RetroContractNo")
	private String retroContractNo;
	@JsonProperty("GwpiOS")
	private String 	gwpiOS;
	@JsonProperty("Sumofpaidpremium")
	private String sumofpaidpremium;
	@JsonProperty("SourceId")
	private String sourceId;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("InsDate")
	private String 	insDate;
	@JsonProperty("PreamendmentDate")
	private String preamendmentDate;
	@JsonProperty("MaxDate")
	private String maxDate;
	@JsonProperty("AcceptenceDate")
	private String 	acceptenceDate;
	@JsonProperty("UserType")
	private String userType;
	@JsonProperty("AccountPeriod")
	private String accountPeriod;
	
	@JsonProperty("AccountPeriodyear")
	private String accountPeriodyear;
	@JsonProperty("Commission")
	private String commission;
	@JsonProperty("Mdpremium")
	private String mdpremium;
	@JsonProperty("Adjustmentpremium")
	private String 	adjustmentpremium;
	@JsonProperty("Recuirementpremium")
	private String recuirementpremium;
	@JsonProperty("GnpiDate")
	private String gnpiDate;
	@JsonProperty("BaseCurrencyId")
	private String baseCurrencyId;
	@JsonProperty("BaseCurrencyName")
	private String baseCurrencyName;
	@JsonProperty("Adjustmentpremiumtemp")
	private String adjustmentpremiumtemp;
	@JsonProperty("EPIourshareview")
	private String ePIourshareview;


}
