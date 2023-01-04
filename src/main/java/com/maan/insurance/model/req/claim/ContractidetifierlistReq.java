package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractidetifierlistReq {
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("CedingCompanyCode")
	private String cedingCompanyCode;
	
	@JsonProperty("UnderwritingYear")
	private String underwritingYear;
	
	@JsonProperty("DeptId")
	private String deptId;
	@JsonProperty("BrokerCode")
	private String brokerCode;
	
}
