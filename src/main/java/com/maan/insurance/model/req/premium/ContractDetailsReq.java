package com.maan.insurance.model.req.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.premium.ContractDetailsRes;
import com.maan.insurance.model.res.premium.ContractDetailsRes1;

import lombok.Data;

@Data
public class ContractDetailsReq {
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("ProductId")
	private String productId; 
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("TransactionNo")
	private String transactionNo;
}
