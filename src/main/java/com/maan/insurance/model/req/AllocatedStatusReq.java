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
public class AllocatedStatusReq {
	
	@JsonProperty("AlloccurrencyId")
	private String alloccurrencyId;
	
	@JsonProperty("PayRecNo")
	private String payRecNo;
	
	@JsonProperty("BrokerName")
	private String brokerName;
	
	@JsonProperty("BrokerId")
	private String brokerId;
	
	@JsonProperty("BranchCode")
	private String branchCode;

}
