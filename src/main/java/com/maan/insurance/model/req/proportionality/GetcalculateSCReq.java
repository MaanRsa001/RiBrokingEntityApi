package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.RiskDetailsEditModeRes1;

import lombok.Data;

@Data
public class GetcalculateSCReq {
	@JsonProperty("ScaleminRatio")
	private String scaleminRatio;

	@JsonProperty("ScalemaxRatio")
	private String scalemaxRatio;
	
	@JsonProperty("Scalebanding")
	private String scalebanding;
	
	@JsonProperty("Scaledigit")
	private String scaledigit;
	@JsonProperty("Scalecombine")
	private String scalecombine;

}
