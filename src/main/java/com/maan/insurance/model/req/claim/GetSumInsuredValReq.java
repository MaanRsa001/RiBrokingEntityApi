package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetSumInsuredValReq {
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("PolicyContractNo")
	private String PolicyContractNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProposalNo")
	private String ProposalNo;
}
