package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateMailDetailsReq1 {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ReinsurerId")
	private String reinsurerId;
	@JsonProperty("BrokerId")
	private String brokerId;
}
