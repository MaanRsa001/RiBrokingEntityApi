package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class ContractidetifierlistReq {
	
	@JsonProperty("TransactionType")
	private String transactionType;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	
	@JsonProperty("UnderwritingYear")
	private String underwritingYear;
	
	@JsonProperty("DeptId")
	private String deptId;
	
	@JsonProperty("BrokerCode")
	private String brokerCode;
}
