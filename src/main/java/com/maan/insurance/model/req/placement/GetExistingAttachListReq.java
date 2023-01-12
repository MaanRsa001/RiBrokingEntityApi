package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.placement.GetExistingAttachListRes1;

import lombok.Data;

@Data
public class GetExistingAttachListReq {
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("EproposalNo")
	private String eproposalNo; 
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId; 
	@JsonProperty("CorresId")
	private String corresId; 
}
