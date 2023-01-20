package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.GetScaleCommissionListRes1;

import lombok.Data;

@Data
public class InsertSlidingScaleMentodInfoReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ProvisionCom")
	private String provisionCom;
	@JsonProperty("Scalementhod")
	private String scalementhod;
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
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("ReferenceNo")
	private String referenceNo;
	@JsonProperty("ScalelossratioFrom")
	private String scalelossratioFrom;
	@JsonProperty("ScalelossratioTo")
	private String scalelossratioTo;
	@JsonProperty("Scaledeltalossratio")
	private String scaledeltalossratio;
	@JsonProperty("Scaledeltacommission")
	private String scaledeltacommission;
	
}
