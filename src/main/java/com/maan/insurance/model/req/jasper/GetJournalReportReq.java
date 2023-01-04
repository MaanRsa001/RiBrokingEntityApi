package com.maan.insurance.model.req.jasper;

import java.io.OutputStream;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetJournalReportReq {
	@JsonProperty("StartDate")
	public String startDate;
	
	@JsonProperty("EndDate")
	public String endDate;
	
	@JsonProperty("BranchCode")
	public String branchCode;
	
	@JsonProperty("JournalId")
	public String journalId; 
	
	@JsonProperty("Status")
	public String status;
	
	@JsonProperty("ProcessStatus")
	public String processStatus;
	
//	@JsonProperty("DownloadType")
//	public String downloadType; 
//	
//	@JsonProperty("OS")
//	public OutputStream os;
}
