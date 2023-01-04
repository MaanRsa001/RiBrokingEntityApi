package com.maan.insurance.model.req.jasper;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetClaimOslrReportReq {
	@JsonProperty("ImagePath")
	public String imagePath;
	
	@JsonProperty("SysDate")
	public String sysDate;
	
	@JsonProperty("ReportName")
	public String reportName;
	
	@JsonProperty("BranchCode")
	public String branchCode; 
	@JsonProperty("StartDate")
	public String startDate;
	
}
