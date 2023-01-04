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
public class GetReceiptGenerationReq {
	
	
	@JsonProperty("Broker")
	private String broker;

	
	@JsonProperty("SerialNo")
	private String serialNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
}
