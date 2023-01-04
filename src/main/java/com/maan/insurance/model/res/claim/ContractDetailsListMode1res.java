package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsListMode1res {
	
	@JsonProperty("PolicyContractNo")
	private String policyContractNo;
	@JsonProperty("DepartmentName")
	private String departmentName;
	@JsonProperty("SignedShare")
	private String signedShare;
	@JsonProperty("From")
	private String from;
	@JsonProperty("To")
	private String to;
	
	@JsonProperty("SumInsOSOC")
	private String sumInsOSOC;
	@JsonProperty("SumInsOSDC")
	private String sumInsOSDC;
	@JsonProperty("AcceptenceDate")
	private String acceptenceDate;
	
	@JsonProperty("AmendId")
	private String amendId;
	
	@JsonProperty("CedingcompanyName")
	private String cedingcompanyName;
	
	@JsonProperty("CedingCompanyCode")
	private String cedingCompanyCode;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("DepartmentClass")
	private String departmentClass;
	
	@JsonProperty("UwYear")
	private String uwYear;
	
	@JsonProperty("Currecny")
	private String currecny;
	
	@JsonProperty("LimitOurshareUSD")
	private String limitOurshareUSD;
	
	@JsonProperty("LimitOrigCurr")
	private String limitOrigCurr;
	
	@JsonProperty("SubProfitCenter")
	private String subProfitCenter;
	
	@JsonProperty("TreatyName")
	private String treatyName;
	
	@JsonProperty("Brokercode")
	private String brokercode;
	
	@JsonProperty("Retention")
	private String retention;
	@JsonProperty("BrokerName")
	private String brokerName;
	
	@JsonProperty("ClaimdepartId")
	private String claimdepartId;
	
	@JsonProperty("ConsubProfitId")
	private String consubProfitId;
	
	@JsonProperty("InsuredName")
	private String insuredName;
	
	@JsonProperty("ProposalType")
	private String proposalType;
	
	@JsonProperty("Basis")
	private String basis;
	
	@JsonProperty("NatureofCoverage")
	private String natureofCoverage;
	
	@JsonProperty("ReinstatementPremium")
	private String reinstatementPremium;
	
	@JsonProperty("CashLossOSOC")
	private String cashLossOSOC;
	
	@JsonProperty("CashLossOSDC")
	private String cashLossOSDC;

}
