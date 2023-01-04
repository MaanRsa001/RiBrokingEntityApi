package com.maan.insurance.model.req.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class XolcoverSNoReq {
	@JsonProperty("XolcoverdepartId")
	private String xolcoverdepartId;
	
	@JsonProperty("XolcoversubdepartId")
	private String xolcoversubdepartId;
	
	@JsonProperty("XolcoverLimitOC")
	private String xolcoverLimitOC;
	
	@JsonProperty("XoldeductableLimitOC")
	private String xoldeductableLimitOC;
	
	@JsonProperty("XolpremiumRateList")
	private String xolpremiumRateList;
	
	@JsonProperty("XolgwpiOC")
	private String xolgwpiOC;
	
	
}
