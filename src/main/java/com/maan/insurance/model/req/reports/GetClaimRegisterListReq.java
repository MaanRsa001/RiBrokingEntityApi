package com.maan.insurance.model.req.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetClaimRegisterListReq {
	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("EndDate")
	private String endDate;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("CedingId")
	private String cedingId;
	@JsonProperty("UwYear")
	private String uwYear;

}
