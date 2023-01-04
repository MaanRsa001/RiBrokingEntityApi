package com.maan.insurance.model.req.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.premium.GetCashLossCreditRes1;

import lombok.Data;

@Data
public class GetCashLossCreditReq {
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("ClaimPayNo")
	private String claimPayNo;
	@JsonProperty("Brokerage")
	private String brokerage;
	@JsonProperty("Tax")
	private String tax;
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("Mode")
	private String mode;
//	@JsonProperty("GetCashLossCreditReq2")
//	private List<GetCashLossCreditReq2> 	getCashLossCreditReq2;
	
	@JsonProperty("GetCashLossCreditReq1")
	private List<GetCashLossCreditReq1> 	getCashLossCreditReq1;


	


}
