package com.maan.insurance.model.req.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetallocationReportListReq {
	@JsonProperty("SettlementType")
	private String settlementType;
	@JsonProperty("StartDate")
	private String startDate;
	@JsonProperty("EndDate")
	private String endDate;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("AllocateStatus")
	private String allocateStatus;
	@JsonProperty("CedingId")
	private String cedingId;

}
