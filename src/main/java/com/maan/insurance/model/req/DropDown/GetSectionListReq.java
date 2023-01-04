package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetSectionListReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ContractNo")
	private String contractNo;

	@JsonProperty("DepartId")
	private String departId;
}
