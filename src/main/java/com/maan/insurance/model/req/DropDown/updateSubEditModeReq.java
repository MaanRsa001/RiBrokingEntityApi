package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class updateSubEditModeReq {
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("UpdateProposalNo")
	private String updateProposalNo;
	
	

}
