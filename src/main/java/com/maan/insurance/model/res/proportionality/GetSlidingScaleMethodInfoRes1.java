package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetSlidingScaleMethodInfoRes1 {
	@JsonProperty("ProvisionCom")
	private String provisionCom;
	@JsonProperty("Scalementhod")
	private String scalementhod;
	@JsonProperty("ScaleminRatio")
	private String scaleminRatio;
	@JsonProperty("ScalemaxRatio")
	private String scalemaxRatio;
	@JsonProperty("Scalecombine")
	private String scalecombine;
	@JsonProperty("Scalebanding")
	private String scalebanding;
	@JsonProperty("Scaledigit")
	private String scaledigit;
	@JsonProperty("ScalelossratioFrom")
	private String scalelossratioFrom; 
	@JsonProperty("ScalelossratioTo")
	private String scalelossratioTo;
	@JsonProperty("Scaledeltalossratio")
	private String scaledeltalossratio;
	@JsonProperty("Scaledeltacommission")
	private String scaledeltacommission;
}
