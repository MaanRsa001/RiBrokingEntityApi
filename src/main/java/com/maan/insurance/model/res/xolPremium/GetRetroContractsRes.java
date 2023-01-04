package com.maan.insurance.model.res.xolPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.premium.GetRetroContractsRes1;

import lombok.Data;
@Data
public class GetRetroContractsRes {
	
	@JsonProperty("Result")
	private List<GetRetroContractsRes1> response;
	//<List<Map(String,Object)>>
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<ErrorCheck> errors;

	@JsonProperty("ErroCode")
	private int erroCode;

	

}
