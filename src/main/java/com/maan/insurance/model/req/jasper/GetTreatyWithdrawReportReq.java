package com.maan.insurance.model.req.jasper;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetTreatyWithdrawReportReq {
	@JsonProperty("UwYear")
	public String uwYear;
	
	@JsonProperty("EndDate")
	public String endDate;
	
	@JsonProperty("TreatyMainClass")
	public String treatyMainClass;
	
	@JsonProperty("BranchCode")
	public String branchCode;
	
	@JsonProperty("BrokerId")
	public String brokerId; 
	
	@JsonProperty("CedingId")
	public String cedingId;

	@JsonProperty("ReportName")
	public String reportName;
	
	@JsonProperty("TreatyType")
	public String treatyType;
	
	
}
