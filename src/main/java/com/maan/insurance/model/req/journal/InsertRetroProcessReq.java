package com.maan.insurance.model.req.journal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertRetroProcessReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("EndDate")
	private String endDate;
	@JsonProperty("Type")
	private String type;
}
