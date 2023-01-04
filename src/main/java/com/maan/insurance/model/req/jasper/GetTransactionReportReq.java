package com.maan.insurance.model.req.jasper;

import java.io.ByteArrayOutputStream;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetTransactionReportReq {


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

	
	@JsonProperty("DocType")
	public String docType;
	
	@JsonProperty("UwYear")
	public String uwYear;
	
	@JsonProperty("ContractNo")
	public String contractNo;
	
	@JsonProperty("LoginName")
	public String loginName;

}
