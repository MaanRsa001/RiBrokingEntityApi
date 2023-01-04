package com.maan.insurance.model.res;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@ Data
public class GetDirectCedingRes {

	@JsonProperty("Code")
	private String code;
	
	@JsonProperty("CodeDescription")
	private String codeDescription;
}
