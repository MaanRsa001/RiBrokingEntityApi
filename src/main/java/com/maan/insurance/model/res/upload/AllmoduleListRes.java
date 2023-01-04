package com.maan.insurance.model.res.upload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.home.GetMenuDropDownListRes;
import com.maan.insurance.model.res.home.GetMenuDropDownListRes1;

import lombok.Data;

@Data
public class AllmoduleListRes {
	@JsonProperty("Result")
	private List<AllmoduleListRes1> commonResponse;

	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
