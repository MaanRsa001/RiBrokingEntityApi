package com.maan.insurance.model.res.billing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.billing.ReverseInsertReq;
import com.maan.insurance.model.res.ReverseRes;

import lombok.Data;

@Data
public class ReverseInsertRes {
	@JsonProperty("Result")
	private List<ReverseInsertRes1> commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
