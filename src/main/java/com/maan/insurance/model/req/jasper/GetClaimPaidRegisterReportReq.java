package com.maan.insurance.model.req.jasper;

import java.io.ByteArrayOutputStream;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetClaimPaidRegisterReportReq {
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

	@JsonProperty("UwYear")
	public String uwYear;
	
	@JsonProperty("CountryId")
	public String countryId;
	
	@JsonProperty("ProductId")
	public String productId; 
	
	@JsonProperty("LoginId")
	public String loginId;
}
