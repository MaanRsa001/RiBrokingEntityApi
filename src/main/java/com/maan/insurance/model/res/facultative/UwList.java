package com.maan.insurance.model.res.facultative;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UwList {
	@JsonProperty("Contdet1")
	private String contdet1;
	
	@JsonProperty("Contdet2")
	private String contdet2;
}
