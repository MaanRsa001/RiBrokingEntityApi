package com.maan.insurance.model.res.claim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;
@Data
public class CommonResponse {

	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<ErrorCheck> errorMessage;//validation

	
	@JsonProperty("Result")
	private Object commonResponse;//successRes
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
