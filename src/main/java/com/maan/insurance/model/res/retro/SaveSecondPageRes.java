package com.maan.insurance.model.res.retro;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.nonproportionality.SaveSecondPageRes1;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;

import lombok.Data;

@Data
public class SaveSecondPageRes {
	@JsonProperty("Result")
	private SaveSecondPageRes1 commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
