package com.maan.insurance.model.res.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SecondpagesaveResp {
	
	
	@JsonProperty("ContractGendration")
	private String contractGendration;
	@JsonProperty("ProStatus")
	private String proStatus;
	@JsonProperty("SharSign")
	private String sharSign;
	@JsonProperty("ContractNo")
	private String ContractNo;
	
}
