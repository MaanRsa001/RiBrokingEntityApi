package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ContractDetailsReq {
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("SectionNo")
	private String sectionNo;
	
	@JsonProperty("ProductId")
	private String productId; 
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("TransactionNo")
	private String transactionNo;
}
