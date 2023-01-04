package com.maan.insurance.model.res.DropDown;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetShareEqualValidationRes 
{

	
	
	@JsonProperty("Result")
	private Boolean commonResponse;
		
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
	
	

}
