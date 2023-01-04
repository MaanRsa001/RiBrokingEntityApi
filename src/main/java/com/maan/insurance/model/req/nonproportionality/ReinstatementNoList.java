package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReinstatementNoList {
	
	@JsonProperty("ReinstatementNo")
	private String reinstatementNo;
	
	@JsonProperty("ReinstatementTypeId")
	private String reinstatementTypeId;
	
	@JsonProperty("ReinstatementAmount")
	private String reinstatementAmount;
	
	@JsonProperty("ReinstatementMinAmount")
	private String reinstatementMinAmount;
	
	@JsonProperty("ReinstatementMinTime")
	private String reinstatementMinTime;
}
