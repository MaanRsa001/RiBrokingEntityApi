package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.DropDown.GetBaseLayerExistingListRes;
import com.maan.insurance.model.res.DropDown.GetBaseLayerExistingListRes1;

import lombok.Data;

@Data
public class EditPlacingDetailsRes {
	@JsonProperty("Result")
	private EditPlacingDetailsResponse commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<ErrorCheck> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
