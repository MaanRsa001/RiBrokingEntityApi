package com.maan.insurance.model.res.journal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class GetJournalViewsRes1 {
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
//	@JsonProperty("JournalId")
//	private String journalId;
	
	@JsonProperty("Ledger")
	private String ledger;
	@JsonProperty("Debitoc")
	private String debitoc;
	@JsonProperty("Creditoc")
	private String creditoc;
	@JsonProperty("Debitugx")
	private String debitugx;
	@JsonProperty("Creditugx")
	private String creditugx;
	@JsonProperty("EndDate")
	private String endDate;
}
