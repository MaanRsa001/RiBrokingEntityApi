package com.maan.insurance.model.req.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.nonproportionality.BonusReq;

import lombok.Data;

@Data
public class MoveBonusReq {
	@JsonProperty("Endorsmentno")
	private String endorsmentno;
	
	@JsonProperty("BonusReq")
	private List<BonusReq> BonusReq;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("BonusTypeId")
	private String bonusTypeId;
	
	@JsonProperty("AcqBonus")
	private String acqBonus;
	
	@JsonProperty("Loginid")
	private String loginid;
	
	
}
