package com.maan.insurance.model.req.jasper;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPayRecReportReq {
	@JsonProperty("StartDate")
	public String startDate;
	
	@JsonProperty("EndDate")
	public String endDate;
	
	@JsonProperty("BranchCode")
	public String branchCode;
	
	@JsonProperty("BrokerId")
	public String brokerId; 
	
	@JsonProperty("CedingId")
	public String cedingId;
	
	@JsonProperty("PayrecType")
	public String payrecType;
	
	@JsonProperty("TransactionType")
	public String transactionType;  
	
//	@JsonProperty("BOS")
//	public ByteArrayOutputStream bos;
	
	@JsonProperty("ShowAllFields")
	public String showAllFields;
	
	@JsonProperty("ReportName")
	public String reportName;
	
	@JsonProperty("BrokerName")
	public String brokerName;
	
	@JsonProperty("CedingCoName")
	public String cedingCoName;
	
	@JsonProperty("SysDate")
	public String sysDate;
	
	@JsonProperty("ProductName")
	public String productName;
	
//	@JsonProperty("DownloadType")
//	public String downloadType;
}
