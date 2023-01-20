package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.GetSectionEditModeRes1;

import lombok.Data;

@Data
public class GetSectionDuplicationCheckReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;

	@JsonProperty("SectionNo")
	private String sectionNo;

	@JsonProperty("BaseLayer")
	private String baseLayer;
	
}
