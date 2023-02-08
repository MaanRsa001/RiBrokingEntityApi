package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FirstpageupdateRes {

	
	@JsonProperty("Result")
	private String status;

	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
