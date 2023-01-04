package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RetroList {
	@JsonProperty("RetroYear")
	private String retroYear;
	@JsonProperty("RetroCeding")
	private String retroCeding;
	@JsonProperty("PercentRetro")
	private String percentRetro;

}
