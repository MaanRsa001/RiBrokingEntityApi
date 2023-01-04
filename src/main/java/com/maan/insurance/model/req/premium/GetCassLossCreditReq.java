package com.maan.insurance.model.req.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetCassLossCreditReq {

	
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("Mode")
	private String mode;
	@JsonProperty("ClaimPayNo")
	private String claimPayNo;

	@JsonProperty("GetCashLossCreditReq1")
	private List<GetCashLossCreditReq1> 	getCashLossCreditReq1;
	

}
