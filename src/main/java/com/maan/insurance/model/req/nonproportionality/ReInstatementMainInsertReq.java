package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReInstatementMainInsertReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("ReInstatementPremium")
	private String reInstatementPremium;
	
	@JsonProperty("ReinstatementNo")
	private List<String> reinstatementNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("AmendId")
	private String amendId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("LoginId")
	private String loginId;

	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("CoverdepartIdList")
	private List<CoverdepartIdList> coverdepartIdList;
}
