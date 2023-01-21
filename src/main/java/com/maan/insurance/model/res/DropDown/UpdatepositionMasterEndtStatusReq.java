package com.maan.insurance.model.res.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdatepositionMasterEndtStatusReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("EndtDate")
	private String endtDate;
	@JsonProperty("CeaseStatus")
	private String ceaseStatus;
}
