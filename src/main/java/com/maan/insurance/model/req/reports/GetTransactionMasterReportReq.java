package com.maan.insurance.model.req.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetTransactionMasterReportReq {
	@JsonProperty("DocType")
	private String docType;
	@JsonProperty("StartDate")
	private String startDate;
	@JsonProperty("EndDate")
	private String endDate;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("CedingId")
	private String cedingId;
	@JsonProperty("UwYear")
	private String uwYear;
}
