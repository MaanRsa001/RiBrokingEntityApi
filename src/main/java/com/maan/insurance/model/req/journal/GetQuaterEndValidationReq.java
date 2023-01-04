package com.maan.insurance.model.req.journal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetQuaterEndValidationReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("Sampledate")
	private String sampledate;
}
