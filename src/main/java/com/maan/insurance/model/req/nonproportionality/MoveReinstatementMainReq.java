package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MoveReinstatementMainReq {
	@JsonProperty("AmendId")
	private String amendId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ReinstatementNo")
	private List<ReinstatementNoList> reinstatementNo;
	
	@JsonProperty("CoverdepartIdList")
	private List<CoverdepartIdList> coverdepartIdList;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("ReinstatementOption")
	private String reinstatementOption; 
	
	@JsonProperty("ReferenceNo")
	private String referenceNo;
}
