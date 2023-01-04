package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AllocListReq {
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("TransactionNo")
	private String transactionNo;

}
