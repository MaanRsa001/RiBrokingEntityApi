package com.maan.insurance.model.req.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.premium.GetPremiumedListRes;
import com.maan.insurance.model.res.premium.GetPremiumedListRes1;

import lombok.Data;

@Data
public class GetConstantPeriodDropDownReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("CategoryId")
	private String categoryId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("ContractNo")
	private String contractNo;
}
