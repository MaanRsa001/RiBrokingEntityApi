package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RetroFinalListres {
	@JsonProperty("CONTDET1")
	private String cONTDET1;
	
	@JsonProperty("CONTDET2")
	private String cONTDET2;
	
	@JsonProperty("RetroDupYerar")
	private String retroDupYerar;
	
	@JsonProperty("RetroDupContract")
	private String retroDupContract;
}
