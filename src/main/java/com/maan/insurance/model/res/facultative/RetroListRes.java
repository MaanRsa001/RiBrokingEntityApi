package com.maan.insurance.model.res.facultative;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.RetroFinalListres;

import lombok.Data;

@Data
public class RetroListRes {
	@JsonProperty("CONTDET1")
	private String cONTDET1;
	
	@JsonProperty("CONTDET2")
	private String cONTDET2;
}
