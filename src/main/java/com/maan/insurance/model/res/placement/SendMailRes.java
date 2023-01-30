package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class SendMailRes {
	
	@JsonProperty("Result")
	private String response;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<ErrorCheck> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;

}
