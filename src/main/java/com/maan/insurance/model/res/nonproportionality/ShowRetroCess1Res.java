package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.BaseLayerStatusRes1;
import com.maan.insurance.model.res.proportionality.GetCrestaDetailListRes1;

import lombok.Data;

@Data
public class ShowRetroCess1Res {
	@JsonProperty("Result")
	private List<RetroCessListRes> commonResponse;
		
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
