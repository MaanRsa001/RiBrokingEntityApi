package com.maan.insurance.model.res.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.GetSectionDuplicationCheckReq;

import lombok.Data;

@Data
public class ConvertPolicyRes1 {
	@JsonProperty("ContNo")
	private String contNo;

	@JsonProperty("SharSign")
	private String sharSign;

	@JsonProperty("ProStatus")
	private String proStatus; 
	
	@JsonProperty("BaseLayerYN")
	private String baseLayerYN; 
	@JsonProperty("ContractGendration")
	private String contractGendration;
	@JsonProperty("ProductId")
	private String productId;
}
