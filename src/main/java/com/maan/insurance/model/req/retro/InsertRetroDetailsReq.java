package com.maan.insurance.model.req.retro;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertRetroDetailsReq {
	@JsonProperty("StartDate")
	private String startDate;
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("EndDate")
	private String endDate;
}
