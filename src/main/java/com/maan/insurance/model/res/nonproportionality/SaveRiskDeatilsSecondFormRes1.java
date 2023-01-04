package com.maan.insurance.model.res.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.GetClassLimitDetailsReq;

import lombok.Data;

@Data
public class SaveRiskDeatilsSecondFormRes1 {
	@JsonProperty("ProStatus")
	private String proStatus;
	
	@JsonProperty("SharSign")
	private String sharSign;
	
	@JsonProperty("ContNo")
	private String contNo;
	
	
//	@JsonProperty("BaseLayerYN")
//	private String baseLayerYN;
	
	@JsonProperty("ContractGendration")
	private String contractGendration;
}
