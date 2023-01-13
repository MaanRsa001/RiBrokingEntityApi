package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetMailToListReq {
	@JsonProperty("CurrentStatus")
	private String currentStatus;
	@JsonProperty("NewStatus")
	private String newStatus;
	@JsonProperty("CedingId")
	private String cedingId;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("ReinsurerId")
	private String reinsurerId;
}
