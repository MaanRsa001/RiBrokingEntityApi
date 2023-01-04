package com.maan.insurance.model.req.journal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetEndDateStatusReq {
	@JsonProperty("EndDate")
	private String endDate;
	@JsonProperty("BranchCode")
	private String branchCode;
}
