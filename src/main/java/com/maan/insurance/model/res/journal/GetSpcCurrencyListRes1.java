package com.maan.insurance.model.res.journal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.journal.InsertInActiveOpenPeriodReq;

import lombok.Data;

@Data
public class GetSpcCurrencyListRes1 {
	@JsonProperty("UWY")
	private String uwy;
	@JsonProperty("SPC")
	private String spc;
	@JsonProperty("CURRENCY")
	private String currency;
	@JsonProperty("StartDate")
	private String startDate;
	@JsonProperty("Narration")
	private String narration;
	@JsonProperty("Reference")
	private String reference;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("JournalId")
	private String journalId;
}
