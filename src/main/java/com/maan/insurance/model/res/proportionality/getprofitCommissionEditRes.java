package com.maan.insurance.model.res.proportionality;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class getprofitCommissionEditRes {
	
	@JsonProperty("getprofitCommissionEditRes1")
	private List<getprofitCommissionEditRes1> getprofitCommissionEditRes1;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;

}
