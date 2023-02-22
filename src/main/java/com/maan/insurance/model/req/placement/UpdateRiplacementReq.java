package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdateRiplacementReq {
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("ApproverStatus")
	private String approverStatus; 
	@JsonProperty("ApproverLoginId")
	private String approverLoginId; 
	@JsonProperty("Remarks")
	private String remarks ;
	
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId; 
	@JsonProperty("BranchCode")
	private String branchCode; 
}
