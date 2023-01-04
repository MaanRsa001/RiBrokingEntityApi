package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProfitCommissionListReq {

	@JsonProperty("Proposalno")
	private String proposalno;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("CommissionType")
	private String commissionType;
	
	@JsonProperty("ContractNo")
	private String contractNo;
}
