package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetInwardBusinessTypeDropDownReq 
{
	
	@JsonProperty("CategoryId")
	private String categoryId;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;

	@JsonProperty("TransactionNo")
	private String transactionNo;

	@JsonProperty("Type")
	private String type;

	@JsonProperty("Mode")
	private String mode;

}
