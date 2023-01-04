package com.maan.insurance.model.res.retro;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.RetroCessListRes;
import com.maan.insurance.model.res.nonproportionality.ShowRetroCess1Res;

import lombok.Data;

@Data
public class ShowSecondPageDataRes {
	@JsonProperty("Result")
	private ShowSecondPageDataRes1 commonResponse;
		
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
