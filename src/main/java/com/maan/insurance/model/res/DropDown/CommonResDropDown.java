package com.maan.insurance.model.res.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class CommonResDropDown 
{
	@JsonProperty("Code")
	private String code;
	
	@JsonProperty("CodeDescription")
	private String codeDescription;

	@JsonProperty("CodeValue")
	private String codeValue;


}
