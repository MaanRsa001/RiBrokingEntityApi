package com.maan.insurance.model.req.home;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.home.GetDepartmentListRes1;

import lombok.Data;

@Data
public class GetOldProductIdReq {
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("ProcessId")
	private String processId;
}
