package com.maan.insurance.model.res.home;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.facultative.GetRetroContractDetailsListRes1;
import com.maan.insurance.model.res.facultative.GetRetroContractDetailsRes;

import lombok.Data;

@Data
public class GetMenuDropDownListRes {
	@JsonProperty("Result")
	private List<GetMenuDropDownListRes1> commonResponse;

	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
