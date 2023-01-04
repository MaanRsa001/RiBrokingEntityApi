package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageDataRes1;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageDataRes;

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
