package com.maan.insurance.model.res.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.res.facultative.ShowSecondpageEditItemsRes1;

import lombok.Data;

@Data
public class ShowSecondpageEditItemsRes {
	@JsonProperty("Result")
	private ShowSecondpageEditItemsRes1 commonResponse;
		
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
