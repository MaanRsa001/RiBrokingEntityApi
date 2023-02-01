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
	
	@JsonProperty("ScFistpc")	
	private String scFistpc;
	
	@JsonProperty("ScProfitMont")	
	private String scProfitMont;
	
	@JsonProperty("ScSubpc")	
	private String scSubpc;
	
	@JsonProperty("ScSubProfitMonth")	
	private String scSubProfitMonth;
	
	@JsonProperty("ScSubSeqCalculation")	
	private String scSubSeqCalculation;
	
	@JsonProperty("ScaleList")	
	private List<ScaleList> ScaleList; 
	
	@JsonProperty("ReferenceNo")	
	private String referenceNo; //Ri 
	
	@JsonProperty("Scalementhod")	
	private String scalementhod;  
	
	@JsonProperty("FpcType")	
	private String fpcType;
	
	@JsonProperty("FpcfixedDate")	
	private String fpcfixedDate;
	

	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ProvisionCom")
	private String provisionCom;
	@JsonProperty("ScaleminRatio")
	private String scaleminRatio;
	@JsonProperty("ScalemaxRatio")
	private String scalemaxRatio;
	@JsonProperty("Scalecombine")
	private String scalecombine;
	@JsonProperty("Scalebanding")
	private String scalebanding;
	@JsonProperty("Scaledigit")
	private String scaledigit;
	@JsonProperty("ScalelossratioFrom")
	private String scalelossratioFrom;
	@JsonProperty("ScalelossratioTo")
	private String scalelossratioTo;
	@JsonProperty("Scaledeltalossratio")
	private String scaledeltalossratio;
	@JsonProperty("Scaledeltacommission")
	private String scaledeltacommission;
}
