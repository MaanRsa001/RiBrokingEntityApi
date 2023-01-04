package com.maan.insurance.model.res.retro;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.retro.ShowSecondPageData1Res1;

import lombok.Data;

@Data
public class ShowSecondPageData1Res {
	@JsonProperty("Result")
	private ShowSecondPageData1Res1 commonResponse;
		
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
