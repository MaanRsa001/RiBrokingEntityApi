package com.maan.insurance.model.req.journal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.journal.GetEndDateListRes1;

import lombok.Data;

@Data
public class InsertInActiveOpenPeriodReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("EndDate")
	private String endDate;
	@JsonProperty("Journalname")
	private String journalname;
	@JsonProperty("StartDate")
	private String startDate;
	@JsonProperty("LoginId")
	private String loginId;
}
