package com.maan.insurance.model.res.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.facultative.GetCommonValueRes;

import lombok.Data;

@Data
public class GetDepartmentIdRes1 {
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("ProposalNo")
	private String proposalNo;
}
