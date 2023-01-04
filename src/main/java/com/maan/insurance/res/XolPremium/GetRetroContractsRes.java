package com.maan.insurance.res.XolPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import lombok.Data;
@Data
public class GetRetroContractsRes {
	
	@JsonProperty("Result")
	private GetSPRetroListRes2 response;
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
