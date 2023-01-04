package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsListMode1res {
	
	@JsonProperty("PolicyContractNo")
	private String policyContractNo;
	@JsonProperty("DepartmentName")
	private String departmentName;
	@JsonProperty("SignedShare")
	private String signedShare;
	@JsonProperty("From")
	private String from;
	@JsonProperty("To")
	private String to;
	@JsonProperty("SumInsOSOC")
	private String sumInsOSOC;
	@JsonProperty("SumInsOSDC")
	private String sumInsOSDC;
	@JsonProperty("AcceptenceDate")
	private String acceptenceDate;

}
