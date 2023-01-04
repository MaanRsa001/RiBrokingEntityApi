package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetSubProfitCentreMultiReq {
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("ProductCode")
	private String productCode;
	
	@JsonProperty("DepartmentId")
	private String departmentId;

	@JsonProperty("SubProfitId")
	private String subProfitId;

	
}
