package com.maan.insurance.model.res.reports;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.retro.FirstInsertRes;
import com.maan.insurance.model.res.retro.FirstInsertRes1;

import lombok.Data;
@Data
public class GetMoveMentInitRes {
	@JsonProperty("Result")
	private List<GetMoveMentInitRes1> commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<ErrorCheck> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
