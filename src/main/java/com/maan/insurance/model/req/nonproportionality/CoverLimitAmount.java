package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class CoverLimitAmount {
	
	@JsonProperty("CoverdepartIdS")
	private String coverdepartIdS;
	
	@JsonProperty("CoverLimitAmount")
	private String coverLimitAmount;
	
	@JsonProperty("CoverLimitPercent")
	private String coverLimitPercent;
	
	@JsonProperty("DeductableLimitAmount")
	private String deductableLimitAmount;
	
	@JsonProperty("DeductableLimitPercent")
	private String deductableLimitPercent;
	
	@JsonProperty("EgnpiAsPerOffSlide")
	private String egnpiAsPerOffSlide;

	@JsonProperty("GnpiAsPOSlide")
	private String gnpiAsPOSlide;

}
