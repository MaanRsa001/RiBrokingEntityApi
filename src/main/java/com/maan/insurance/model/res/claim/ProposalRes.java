package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProposalRes {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("DepartmentId")
	private String departmentId;
}
