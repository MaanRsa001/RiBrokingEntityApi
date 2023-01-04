package com.maan.insurance.model.req.jasper;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetJournalViewDownloadReq {
	@JsonProperty("StartDate")
	public String startDate;
	
	@JsonProperty("EndDate")
	public String endDate;
	
	@JsonProperty("Status")
	public String status;
	
	@JsonProperty("BranchCode")
	public String branchCode;
	
	@JsonProperty("JournalType")
	public String journalType; 
	
	@JsonProperty("TypeId")
	public String typeId;

	@JsonProperty("ReportName")
	public String reportName;
	
	
	
}
