package com.maan.insurance.model.req.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.premium.CurrencyListRes;
import com.maan.insurance.model.res.premium.CurrencyListRes1;

import lombok.Data;

@Data
public class GetBonusValueReq {
	@JsonProperty("BonusId")
	private String bonusId;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("Layerno")
	private String layerno;
}
