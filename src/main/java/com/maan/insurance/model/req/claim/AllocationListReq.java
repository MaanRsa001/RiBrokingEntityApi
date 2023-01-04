package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AllocationListReq {
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("TransactionNumber")
	private String transactionNumber;
	
	

}
