package com.maan.insurance.model.res.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetConstantPeriodDropDownRes {
	@JsonProperty("Result")
	private List<GetConstantPeriodDropDownRes1> commonResponse;
	@JsonProperty("SlideScenario")
	private String slideScenario;
		
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
