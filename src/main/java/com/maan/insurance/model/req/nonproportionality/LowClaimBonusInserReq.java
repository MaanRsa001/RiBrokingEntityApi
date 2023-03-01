package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LowClaimBonusInserReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("EndorsmentNo")
	private String endorsmentNo;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("BonusReq")
	private List<BonusReq> bonusReq;
	
	@JsonProperty("BonusTypeId")
	private String bonusTypeId;
	@JsonProperty("AcqBonus")
	private String acqBonus;
	@JsonProperty("LoginId")
	private String loginId;

	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("PolicyBranch")
	private String policyBranch;
	@JsonProperty("CedingCo")
	private String cedingCo;
	@JsonProperty("Broker")
	private String broker;
	@JsonProperty("TreatyNameType")
	private String treatyNameType;
	@JsonProperty("Month")
	private String month;
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("ReferenceNo")
	private String referenceNo; 
}
