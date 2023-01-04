package com.maan.insurance.model.req.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SlideScenarioReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("LayerNo")
	private String layerNo;
	
	
}
