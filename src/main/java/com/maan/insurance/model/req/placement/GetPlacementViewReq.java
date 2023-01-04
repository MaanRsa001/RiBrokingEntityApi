package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetPlacementViewReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("EproposalNo")
	private String eproposalNo;
	@JsonProperty("ReinsurerId")
	private String reinsurerId;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("NewStatus")
	private String newStatus;
}
