package com.maan.insurance.model.req.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class PremiumUpdateMethodRiReq {
	@JsonProperty("ButtonStatus")
	private String buttonStatus;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("Transaction")
	private String Transaction;
	
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("TransactionNo")
	private String transactionNo;
	
	@JsonProperty("LoginId")
	private String  loginId;
	@JsonProperty("RequestNo")
	private String requestNo;
	
	
	@JsonProperty("TableType")
	private String tableType;
	
	@JsonProperty("CountryId")
	private String countryId;
	
	@JsonProperty("AccountPeriod")
	private String accountPeriod;
	
	@JsonProperty("AccountPeriodyear")
	private String accountPeriodyear;
	
	@JsonProperty("CurrencyId")
	private String currencyId;
	
	@JsonProperty("ExchRate")
	private String exchRate;
	
	@JsonProperty("Brokerageview")
	private String  brokerageview;
	@JsonProperty("Brokerage")
	private String brokerage;
	
	
	@JsonProperty("Taxview")
	private String taxview;
	
	@JsonProperty("Tax")
	private String tax;
	
	@JsonProperty("Overrider")
	private String overrider;
	
	@JsonProperty("OverRiderview")
	private String overRiderview;
	
	@JsonProperty("WithHoldingTaxOC")
	private String withHoldingTaxOC;
	
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	
	@JsonProperty("InsDate")
	private String insDate;
	
	@JsonProperty("MaxDate")
	private String maxDate;
	
	@JsonProperty("PremiumQuotaShare")
	private String  premiumQuotaShare;
	
	@JsonProperty("CommissionQuotaShare")
	private String commissionQuotaShare;
	
	@JsonProperty("PremiumSurplus")
	private String premiumSurplus;
	
	@JsonProperty("CommissionSurplus")
	private String commissionSurplus;
	
	@JsonProperty("PremiumportifolioIn")
	private String premiumportifolioIn;
	
	@JsonProperty("RiCession")
	private String riCession;
	
	@JsonProperty("TaxDedectSource")
	private String taxDedectSource;
	
	@JsonProperty("VatPremium")
	private String vatPremium;
	
	@JsonProperty("BrokerageVat")
	private String brokerageVat;
	
	@JsonProperty("DocumentType")
	private String documentType;
	
	@JsonProperty("SlideScaleCom")
	private String  slideScaleCom;

	@JsonProperty("Predepartment")
	private String predepartment;
	
	@JsonProperty("SubProfitId")
	private String subProfitId;
	
	@JsonProperty("AccountPeriodDate")
	private String accountPeriodDate;
	
	@JsonProperty("StatementDate")
	private String statementDate;
	
	@JsonProperty("OsbYN")
	private String osbYN;
	
	@JsonProperty("LossParticipation")
	private String lossParticipation;
	
	@JsonProperty("SectionName")
	private String sectionName;
	
	@JsonProperty("ProposalNo")
	private String  proposalNo;
	@JsonProperty("Mode")
	private String mode;
	
	
	@JsonProperty("Commissionview")
	private String commissionview;
	
	@JsonProperty("CommssionSurp")
	private String commssionSurp;
	
	@JsonProperty("CliamPortfolioin")
	private String cliamPortfolioin;
	
	@JsonProperty("Premiumportifolioout")
	private String premiumportifolioout;
	
	@JsonProperty("LossReserveReleased")
	private String lossReserveReleased;
	
	@JsonProperty("PremiumReserveQuotaShare")
	private String premiumReserveQuotaShare;
	
	@JsonProperty("CashLossCredit")
	private String  cashLossCredit;
	
	@JsonProperty("LossReserveRetained")
	private String lossReserveRetained;
	
	
	@JsonProperty("ProfitCommission")
	private String profitCommission;
	
	@JsonProperty("CashLossPaid")
	private String cashLossPaid;
	
	@JsonProperty("Receiptno")
	private String receiptno;
	
	@JsonProperty("Claimspaid")
	private String claimspaid;
	
	@JsonProperty("Settlementstatus")
	private String settlementstatus;
	
	@JsonProperty("XlCost")
	private String xlCost;
	
	@JsonProperty("Cliamportfolioout")
	private String  cliamportfolioout;
	
	@JsonProperty("PremiumReserveReleased")
	private String premiumReserveReleased;
	
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
	
	@JsonProperty("Interest")
	private String interest;
	
	@JsonProperty("OsClaimsLossUpdateOC")
	private String  osClaimsLossUpdateOC;

	@JsonProperty("Layerno")
	private String layerno;
	
	@JsonProperty("AmendmentDate")
	private String AmendmentDate;

	@JsonProperty("PRTransNo")
	private String pRTransNo;
	
	@JsonProperty("Type")
	private String type;

	@JsonProperty("Currency")
	private String currency;

	@JsonProperty("InsertLossReserved")
	private List<InsertLossReserved> insertLossReserved;
	
	@JsonProperty("InsertPremiumReserved")
	private List<InsertPremiumReserved> insertPremiumReserved;

	@JsonProperty("EnteringMode")
	private String enteringMode;
	@JsonProperty("ShareSigned")
	private String shareSigned;
	@JsonProperty("SectionType")
	private String sectionType;
	@JsonProperty("AcceptenceDate")
	private String acceptenceDate;
	@JsonProperty("SourceId")
	private String sourceId;
	@JsonProperty("M1OC")
	private String m1oc;
	@JsonProperty("M2OC")
	private String m2oc;
	@JsonProperty("M3OC")
	private String m3oc;
	@JsonProperty("ChooseTransaction")
	private String chooseTransaction;
	@JsonProperty("TransDropDownVal")
	private String transDropDownVal;
	@JsonProperty("ClaimPayNo")
	private String claimPayNo;

//	@JsonProperty("GetCashLossCreditReq2")
//	private List<GetCashLossCreditReq2> 	getCashLossCreditReq2;
	
	@JsonProperty("GetCashLossCreditReq1")
	private List<GetCashLossCreditReq1> 	getCashLossCreditReq1;
	
}
