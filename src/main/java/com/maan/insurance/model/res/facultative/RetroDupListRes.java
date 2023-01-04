package com.maan.insurance.model.res.facultative;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RetroDupListRes {
	@JsonProperty("CONTDET1")
	private String cONTDET1;
	
	@JsonProperty("CONTDET2")
	private String cONTDET2;
}
