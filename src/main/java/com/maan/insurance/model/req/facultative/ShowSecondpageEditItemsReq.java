package com.maan.insurance.model.req.facultative;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShowSecondpageEditItemsReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ContractNo")
	private String contractNo;

	@JsonProperty("NoOfInst")
	private String noOfInst;
	
	@JsonProperty("ReceiptofPayment")
	private String receiptofPayment;
}
