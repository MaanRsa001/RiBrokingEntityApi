package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverdepartIdList {
	@JsonProperty("CoverdepartId")
	private String coverdepartId;
	
	@JsonProperty("CoverLimitOC")
	private String coverLimitOC;
	
	
}
