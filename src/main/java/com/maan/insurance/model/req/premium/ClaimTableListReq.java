package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimTableListReq {

//	@JsonProperty("ClaimNo")
//	private String claimNo;
//	
//	@JsonProperty("ProductId")
//	private String productId;
	
	@JsonProperty("PolicyContractNo")
	private String policyContractNo;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
//	@JsonProperty("ProposalNo")
//	private String ProposalNo;
}
