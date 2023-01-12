package com.maan.insurance.model.res.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.reports.GetPendingOffersListRes;
import com.maan.insurance.model.res.reports.GetPendingOffersListRes1;

import lombok.Data;

@Data
public class PremiumListRes {
	@JsonProperty("Result")
	private List<PremiumListRes1> commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<ErrorCheck> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
