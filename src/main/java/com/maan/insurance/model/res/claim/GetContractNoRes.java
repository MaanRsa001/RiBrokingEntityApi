package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetContractNoRes {

	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("ContractNo")
	private String contractNo;
}
