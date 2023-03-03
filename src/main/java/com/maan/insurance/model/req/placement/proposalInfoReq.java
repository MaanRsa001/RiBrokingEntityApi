package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class proposalInfoReq {
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("BaseProposalNo")
	private String BaseProposalNo; 
	@JsonProperty("BouquetNo")
	private String bouquetNo; 
}
