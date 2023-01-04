package com.maan.insurance.model.req.jasper;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetSoaReportReq {
	@JsonProperty("StartDate")
	public String startDate;
	
	@JsonProperty("EndDate")
	public String endDate;
	
	@JsonProperty("ImagePath")
	public String imagePath;
	
	@JsonProperty("BranchCode")
	public String branchCode;
	
	@JsonProperty("BrokerId")
	public String brokerId; 
	
	@JsonProperty("CedingId")
	public String cedingId;

	@JsonProperty("ReportName")
	public String reportName;
	
	@JsonProperty("CedingCoName")
	public String cedingCoName;
	
	@JsonProperty("BrokerName")
	public String brokerName;
	
	@JsonProperty("SysDate")
	public String sysDate;
	
	@JsonProperty("ProductName")
	public String productName;

	@JsonProperty("SettledItems")
	public String settledItems;
	
	@JsonProperty("UnAllocatedCash")
	public String unAllocatedCash;
	
	@JsonProperty("ProductId")
	public String productId; 
	
	@JsonProperty("SaperatePRCLYN")
	public String saperatePRCLYN;
	
	
}
