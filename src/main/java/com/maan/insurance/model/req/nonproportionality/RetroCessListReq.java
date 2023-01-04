package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RetroCessListReq {
	@JsonProperty("CedingId")
	private String cedingId;
	
	@JsonProperty("BrokerId")
	private String brokerId;
	
	@JsonProperty("ProStatus")
	private String proStatus;
	
	@JsonProperty("ShareWritt")
	private String shareWritt;
	
	@JsonProperty("SharSign")
	private String sharSign;
	@JsonProperty("Commission")
	private String 	commission;
}
