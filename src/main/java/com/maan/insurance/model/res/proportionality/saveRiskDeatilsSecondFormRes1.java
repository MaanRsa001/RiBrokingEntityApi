package com.maan.insurance.model.res.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class saveRiskDeatilsSecondFormRes1 {

	@JsonProperty("ProStatus")
	private String proStatus;
	
	@JsonProperty("SharSign")
	private String sharSign;
	
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("BaseLayerYN")
	private String baseLayerYN;
	
	@JsonProperty("ContractGendration")
	private String contractGendration;
	
}
