package com.maan.insurance.model.req.jasper;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetDebtorsAgingReportReq {
	@JsonProperty("StartDate")
	public String startDate;
	
	@JsonProperty("DebTo3")
	public String debTo3;
	
	@JsonProperty("BranchCode")
	public String branchCode;
	
	@JsonProperty("BrokerId")
	public String brokerId; 
	
	@JsonProperty("CedingId")
	public String cedingId;

	@JsonProperty("ReportName")
	public String reportName;
	
	@JsonProperty("Type")
	public String type;
	
	@JsonProperty("DebFrom")
	public String debFrom;
	
	@JsonProperty("DebTo")
	public String debTo;
	
	@JsonProperty("DebFrom1")
	public String debFrom1;

	@JsonProperty("DebTo1")
	public String debTo1;
	
	@JsonProperty("DebFrom2")
	public String debFrom2;
	
	@JsonProperty("ProductId")
	public String productId; 
	
	@JsonProperty("DebTo2")
	public String debTo2;
	
	@JsonProperty("DebFrom3")
	public String debFrom3;
	
	@JsonProperty("DebFrom4")
	public String debFrom4;
	
	@JsonProperty("DebTo4")
	public String debTo4; 
	
	@JsonProperty("DebFrom5")
	public String debFrom5;
	
	@JsonProperty("DebTo5")
	public String debTo5;
	
	@JsonProperty("DocType")
	public String docType;
}
