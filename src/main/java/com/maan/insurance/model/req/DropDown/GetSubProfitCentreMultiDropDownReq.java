package com.maan.insurance.model.req.DropDown;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.premium.ContractDetailsReq;

import lombok.Data;

@Data
public class GetSubProfitCentreMultiDropDownReq {
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("ProductCode")
	private String productCode;
	
	@JsonProperty("DepartmentId")
	private String departmentId;

	@JsonProperty("SubProfitId")
	private String subProfitId;

}
