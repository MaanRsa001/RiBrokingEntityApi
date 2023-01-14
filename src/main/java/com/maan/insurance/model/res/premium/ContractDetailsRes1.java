package com.maan.insurance.model.res.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsRes1 {
	@JsonProperty("ContNo")
	private String contNo;
	
//	@JsonProperty("BranchCode")
//	private String branchCode;
	
	@JsonProperty("AmendId")
	private String amendId;
	
	@JsonProperty("ProductId")
	private String productId; 
	
	@JsonProperty("ProfitCenter")
	private String profitCenter;
	
	@JsonProperty("SubProfitCenter")
	private String subProfitCenter;
	
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("Broker")
	private String broker; 
	@JsonProperty("TreatyNameType")
	private String treatyNameType; 
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("UwYear")
	private String uwYear;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("InsDate")
	private String insDate; 
	@JsonProperty("ExpDate")
	private String expDate;
	
	@JsonProperty("Month")
	private String month;
	
	@JsonProperty("BaseCurrencyId")
	private String baseCurrencyId;
	
	@JsonProperty("BaseCurrencyName")
	private String baseCurrencyName; 
	
	@JsonProperty("PolicyBranch")
	private String policyBranch;
	
	@JsonProperty("Address")
	private String address;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("PreDepartment")
	private String preDepartment; 
	@JsonProperty("ConsubProfitId")
	private String consubProfitId; 
	
	@JsonProperty("TreatyType")
	private String treatyType;
	
	@JsonProperty("BusinessType")
	private String businessType;
	
	@JsonProperty("AcceptenceDate")
	private String acceptenceDate;
	
	@JsonProperty("CommissionView")
	private String commissionView; 
	
	@JsonProperty("PremiumReserveView")
	private String premiumReserveView; 
	
	@JsonProperty("LossReserveView")
	private String lossReserveView; 
	
	@JsonProperty("ProfitCommYN")
	private String profitCommYN;
	
	@JsonProperty("CommissionSurbView")
	private String commissionSurbView;
	
	@JsonProperty("OverRiderView")
	private String overRiderView;
	
	@JsonProperty("BrokerageView")
	private String brokerageView; 
	@JsonProperty("TaxView")
	private String taxView; 
	@JsonProperty("OtherCostView")
	private String otherCostView; 
	
	@JsonProperty("OurAssessmentOfOrginal")
	private String ourAssessmentOfOrginal;
	
	@JsonProperty("PremiumReserve")
	private String premiumReserve;
	
	@JsonProperty("ShareSigned")
	private String shareSigned;
	
	@JsonProperty("PremiumQuotaView")
	private String premiumQuotaView; 
	
	@JsonProperty("PremiumsurpView")
	private String premiumsurpView; 
	
	@JsonProperty("XlCostView")
	private String xlCostView; 
	
	@JsonProperty("RdsExchageRate")
	private String rdsExchageRate;
	
	@JsonProperty("Epioc")
	private String epioc;
	
	@JsonProperty("CurrencyName")
	private String currencyName;
	
	@JsonProperty("SettlementStatus")
	private String settlementStatus; 
	@JsonProperty("SumofPaidPremium")
	private String  sumofPaidPremium;
	@JsonProperty("CedingId")
	private String cedingId;
	@JsonProperty("BrokerId")
	private String brokerId; 
}
