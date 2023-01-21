package com.maan.insurance.model.res.DropDown;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.GetLayerInfoRes;
import com.maan.insurance.model.res.nonproportionality.GetLayerInfoRes1;

import lombok.Data;
@Data
public class GetCompanyInfoRes {
	@JsonProperty("Result")
	private List<GetCompanyInfoRes1> commonResponse;

	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
