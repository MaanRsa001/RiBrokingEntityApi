package com.maan.insurance.model.res.DropDown;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.retro.CommonSaveRes;

import lombok.Data;

@Data
public class GetBouquetListRes {
	@JsonProperty("Result")
	private List<GetBouquetListRes1> commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<ErrorCheck> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
