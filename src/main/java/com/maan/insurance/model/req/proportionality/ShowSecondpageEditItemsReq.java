package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShowSecondpageEditItemsReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("IncepDate")
	private String incepDate;
	
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("NoInsurer")
	private String noInsurer; 
	
	@JsonProperty("SectionMode")
	private String sectionMode;
}
