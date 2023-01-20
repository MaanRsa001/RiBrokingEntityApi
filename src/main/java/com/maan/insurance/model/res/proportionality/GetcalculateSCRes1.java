package com.maan.insurance.model.res.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetcalculateSCRes1 { 
	@JsonProperty("ScaleFrom")
	private String scaleFrom;

	@JsonProperty("ScaleTo")
	private String scaleTo;

	@JsonProperty("ScaleLowClaimBonus")
	private String scaleLowClaimBonus;
}
