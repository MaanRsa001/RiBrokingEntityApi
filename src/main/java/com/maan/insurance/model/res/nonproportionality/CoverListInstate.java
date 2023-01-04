package com.maan.insurance.model.res.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverListInstate {
	@JsonProperty("CoverdepartId")
	private String coverdepartId;
	
	@JsonProperty("CoverLimitOC")
	private String coverLimitOC;
	
	@JsonProperty("CoverLimitAmount")
	private String coverLimitAmount;
}
