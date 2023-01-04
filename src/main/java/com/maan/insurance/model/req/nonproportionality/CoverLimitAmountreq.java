package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverLimitAmountreq {
	
	@JsonProperty("CoverdepartIdS")
	private String coverdepartIdS;
	
	@JsonProperty("CoverLimitAmountreq")
	private String coverLimitAmountreq;
	
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
