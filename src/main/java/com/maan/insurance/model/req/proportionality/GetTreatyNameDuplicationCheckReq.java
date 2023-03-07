package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetTreatyNameDuplicationCheckReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;

	@JsonProperty("TreatyNameType")
	private String treatyNameType;

	@JsonProperty("BaseLayer")
	private String baseLayer;
	
}
