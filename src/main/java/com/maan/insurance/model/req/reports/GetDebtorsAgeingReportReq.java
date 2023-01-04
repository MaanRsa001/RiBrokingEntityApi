package com.maan.insurance.model.req.reports;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.facultative.ShowSecondPagedataReq;

import lombok.Data;

@Data
public class GetDebtorsAgeingReportReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("StartDate")
	private String startDate;
	@JsonProperty("EndDate")
	private String endDate;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("DocType")
	private String docType;
	@JsonProperty("CedingId")
	private String cedingId;
	@JsonProperty("DebFrom")
	private String debFrom;
	@JsonProperty("DebTo")
	private String debTo; 
	@JsonProperty("DebFrom1")
	private String debFrom1;
	@JsonProperty("DebTo1")
	private String debTo1;
	@JsonProperty("DebFrom2")
	private String debFrom2;
	@JsonProperty("DebTo2")
	private String debTo2;
	@JsonProperty("DebFrom3")
	private String debFrom3;
	@JsonProperty("DebTo3")
	private String debTo3;
	@JsonProperty("DebFrom4")
	private String debFrom4;
	@JsonProperty("DebTo4")
	private String debTo4;
	@JsonProperty("DebFrom5")
	private String debFrom5;
	@JsonProperty("DebTo5")
	private String debTo5;
}
