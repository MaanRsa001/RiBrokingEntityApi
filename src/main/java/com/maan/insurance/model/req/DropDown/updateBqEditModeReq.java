package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class updateBqEditModeReq {
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("Val")
	private String val;

	@JsonProperty("UpdateProposalNo")
	private String updateProposalNo;

}
