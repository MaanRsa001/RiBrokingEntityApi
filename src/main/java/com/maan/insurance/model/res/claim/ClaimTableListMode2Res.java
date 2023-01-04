package com.maan.insurance.model.res.claim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.ClaimlistRes;

import lombok.Data;

@Data
public class ClaimTableListMode2Res {

	@JsonProperty("Result")
	private List<ClaimTableListMode2ResList> commonResponse;
	
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
	
//	@JsonProperty("CurrencyName")
//	private String currencyName;
//		
//	@JsonProperty("TotalORpaidAmount")
//	private String totalORpaidAmount;
//	
//	@JsonProperty("TotalSApaidAmount")
//	private String totalSApaidAmount;
//	
//	@JsonProperty("TotalOPpaidAmount")
//	private String totalOPpaidAmount;
//	
//	@JsonProperty("TotalTORpaidAmount")
//	private String totalTORpaidAmount;
	
}
