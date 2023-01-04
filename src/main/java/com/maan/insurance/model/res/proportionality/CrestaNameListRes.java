package com.maan.insurance.model.res.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CrestaNameListRes {
	@JsonProperty("Code")
	private String code;
	
	@JsonProperty("CodeDescribtion")
	private String codeDescribtion;
}
