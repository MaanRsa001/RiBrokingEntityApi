package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ScaleCommissionInsertReq {

	@JsonProperty("AmendId")	
	private String amendId;
	
	@JsonProperty("ProposalNo")	
	private String proposalNo;
	
	@JsonProperty("BranchCode")	
	private String branchCode;

	@JsonProperty("LayerNo")	
	private String layerNo;
	
	@JsonProperty("Type")	
	private String type;
	
	@JsonProperty("ContractNo")	
	private String contractNo;
	
	@JsonProperty("Productid")	
	private String productid;
	
	@JsonProperty("PageFor")	
	private String pageFor;
	
	@JsonProperty("BonusTypeId")	
	private String bonusTypeId;
	
	@JsonProperty("LoginId")	
	private String loginId;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("QuotaShare")	
	private String quotaShare;
	
	@JsonProperty("Bonusremarks")	
	private String bonusremarks;
	
	@JsonProperty("Fistpc")	
	private String fistpc;
	
	@JsonProperty("ProfitMont")	
	private String profitMont;
	
	@JsonProperty("Subpc")	
	private String subpc;
	
	@JsonProperty("SubProfitMonth")	
	private String subProfitMonth;
	
	@JsonProperty("SubSeqCalculation")	
	private String subSeqCalculation;
	
	@JsonProperty("ScaleList")	
	private List<ScaleList> ScaleList;
	

}
