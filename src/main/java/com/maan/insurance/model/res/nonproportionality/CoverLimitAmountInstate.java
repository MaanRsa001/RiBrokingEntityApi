package com.maan.insurance.model.res.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverLimitAmountInstate {
	@JsonProperty("CoverLimitAmount")
	private String coverLimitAmount;
}
