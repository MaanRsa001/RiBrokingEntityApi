package com.maan.insurance.model.req.claim;

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
	
	@JsonProperty("SectionNo")
	private String sectionNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
//	@JsonProperty("ProposalNo")
//	private String ProposalNo;
}
