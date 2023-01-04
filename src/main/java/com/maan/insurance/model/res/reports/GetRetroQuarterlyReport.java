package com.maan.insurance.model.res.reports;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.facultative.RetroDupListRes;
import com.maan.insurance.model.res.facultative.RetroListRes;
import com.maan.insurance.model.res.facultative.ShowSecondPagedataRes1;

import lombok.Data;

@Data
public class GetRetroQuarterlyReport {
	@JsonProperty("Result")
	private List<GetRetroQuarterlyReport1> commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<ErrorCheck> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
