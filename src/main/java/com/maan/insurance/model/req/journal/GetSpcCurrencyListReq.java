package com.maan.insurance.model.req.journal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetSpcCurrencyListReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("EndDate")
	private String endDate;
	@JsonProperty("JournalID")
	private String journalID;
	@JsonProperty("StartDate")
	private String startDate;
	@JsonProperty("Status")
	private String status;
}
