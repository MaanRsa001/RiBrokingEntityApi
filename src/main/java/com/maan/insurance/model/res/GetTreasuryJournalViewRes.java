package com.maan.insurance.model.res;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetTreasuryJournalViewRes {
	
	@JsonProperty("StartDate")
	private String  startDate;

	@JsonProperty("Reference")
	private String  reference;

	@JsonProperty("Ledger")
	private String  ledger;

	@JsonProperty("Uwy")
	private String  uwy;

	@JsonProperty("Spc")
	private String  spc;

	@JsonProperty("Currency")
	private String  currency;

	@JsonProperty("Debitoc")
	private String  debitoc;

	@JsonProperty("Creditoc")
	private String  creditoc;

	@JsonProperty("Debitugx")
	private String  debitugx;

	@JsonProperty("Creditugx")
	private String  creditugx;

	@JsonProperty("Narration")
	private String  narration;

	@JsonProperty("ProductId")
	private String  productId;

	@JsonProperty("EndDate")
	private String  endDate;
}
