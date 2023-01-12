package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CopyDatatoDeleteTableReq {
	@JsonProperty("TransactionNo")
	private String transactionNo;
	
	@JsonProperty("UserName")
	private String userName;
}
