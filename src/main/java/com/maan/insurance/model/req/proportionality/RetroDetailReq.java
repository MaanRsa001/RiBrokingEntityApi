package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RetroDetailReq {

	@JsonProperty("RetroCeding")
	private String retroCeding;
	@JsonProperty("PercentRetro")
	private String percentRetro;
	@JsonProperty("RetroYear")
	private String retroYear;
	

	
	
	
	
	

}
