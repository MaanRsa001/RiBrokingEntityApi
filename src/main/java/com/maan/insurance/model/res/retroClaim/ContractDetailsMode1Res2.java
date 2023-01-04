package com.maan.insurance.model.res.retroClaim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsMode1Res2 {
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
	@JsonProperty("LimitOrigCurr")
	private String limitOrigCurr;
	@JsonProperty("LimitOurshareUSD")
	private String limitOurshareUSD;
	@JsonProperty("SubProfitCenter")
	private String subProfitCenter;
	@JsonProperty("Retention")
	private String retention;
	@JsonProperty("TreatyName")
	private String treatyName;
	@JsonProperty("Brokercode")
	private String brokercode;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("AcceptenceDate")
	private String acceptenceDate;
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
	@JsonProperty("ReinstType")
	private String reinstType;
	@JsonProperty("ReinstPremiumOCOS")
	private String reinstPremiumOCOS;
	@JsonProperty("CashLossOSOC")
	private String cashLossOSOC;
	@JsonProperty("CashLossOSDC")
	private String cashLossOSDC;
	
}
