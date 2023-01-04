package com.maan.insurance.model.req.jasper;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetJournalReportReq1 {
	@JsonProperty("Openperiod")
	public String openperiod;

	
	@JsonProperty("BranchCode")
	public String branchCode;
	
	@JsonProperty("JournalId")
	public String journalId; 
	
	@JsonProperty("Shortname")
	public String shortname;

}
