package com.maan.insurance.model.req.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPayRecRegisterListReq {
	@JsonProperty("TransactionType")
	private String transactionType;
	@JsonProperty("StartDate")
	private String startDate;
	@JsonProperty("EndDate")
	private String endDate;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("PayrecType")
	private String payrecType;
	@JsonProperty("CedingId")
	private String cedingId; 
	@JsonProperty("ShowAllFields")
	private String showAllFields; 
}
