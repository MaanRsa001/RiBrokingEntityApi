package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetClassLimitDetailsRes1 {
	@JsonProperty("CoverdepartId")
	private String coverdepartId;
	
	@JsonProperty("CoverLimitAmount")
	private String coverLimitAmount;
	
	@JsonProperty("CoverLimitPercent")
	private String coverLimitPercent;

	@JsonProperty("DeductableLimitAmount")
	private String deductableLimitAmount;
	
	@JsonProperty("DeductableLimitPercent")
	private String deductableLimitPercent;

	@JsonProperty("Egnpi")
	private String egnpi;
	
	@JsonProperty("Gnpi")
	private String gnpi;
	
	@JsonProperty("TotalLoopCount")
	private String totalLoopCount;
}
