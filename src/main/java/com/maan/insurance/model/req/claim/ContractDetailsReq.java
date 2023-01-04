package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsReq {
//	@JsonProperty("ClaimNo")
//	private String claimNo;
	@JsonProperty("ProductId")
	private String productId;
//	@JsonProperty("PolicyContractNo")
//	private String policyContractNo;
//	
//	@JsonProperty("SumOfPaidAmountOC")
//	private String SumOfPaidAmountOC;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("BranchCode")
	private String branchCode;
//	@JsonProperty("RevSumOfPaidAmt")
//	private String RevSumOfPaidAmt;
//	@JsonProperty("DepartmentId")
//	private String departmentId;
//	@JsonProperty("Currecny")
//	private String currecny;
//	@JsonProperty("InsuredName")
//	private String insuredName;
//	@JsonProperty("LossEstimateOrigCurr")
//	private String lossEstimateOrigCurr;
	
	
	
	
	

}
