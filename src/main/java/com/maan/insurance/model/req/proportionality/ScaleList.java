package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ScaleList {

	@JsonProperty("ScaleFrom")	
	private String scaleFrom;
	
	@JsonProperty("ScaleTo")	
	private String scaleTo;
	
	@JsonProperty("ScaleLowClaimBonus")	
	private String scaleLowClaimBonus;
	
	@JsonProperty("ScaleSNo")	
	private String scaleSNo;
	
	@JsonProperty("ScaleMaxPartPercent")	
	private String scaleMaxPartPercent;
}