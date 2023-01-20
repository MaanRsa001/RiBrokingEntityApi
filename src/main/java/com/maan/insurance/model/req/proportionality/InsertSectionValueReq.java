package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class InsertSectionValueReq {
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("BranchCode")
	private String branchCode; 
}
