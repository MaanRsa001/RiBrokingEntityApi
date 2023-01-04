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
public class AllocateDetailsReq {
	
	@JsonProperty("PayRecNo")
	private String payRecNo;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("Broker")
	private String broker;
	
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	/*
	 * @JsonProperty("SerialNo") private String serialNo;
	 */
}
