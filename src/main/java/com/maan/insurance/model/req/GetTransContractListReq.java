package com.maan.insurance.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTransContractListReq {

	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("ReceivePayAmounts")
	private String receivePayAmounts;
	@JsonProperty("PreviousValue")
	private String previousValue;
	
}
