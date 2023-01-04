package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProposalNoReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("Contarctno")
	private String contarctno;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("LayerNo")
	private String layerNo;
	

}
