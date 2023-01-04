package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetMailToListReq {
	@JsonProperty("CurrentStatus")
	private String currentStatus;
	@JsonProperty("NewStatus")
	private String newStatus;
	@JsonProperty("BrokerCompany")
	private String brokerCompany;
	
	@JsonProperty("CedingCompany")
	private String cedingCompany;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("ReinsurerId")
	private String reinsurerId;
}
