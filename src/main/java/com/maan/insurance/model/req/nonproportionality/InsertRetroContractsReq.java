package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.proportionality.RetroDetailReq;
import com.maan.insurance.model.res.nonproportionality.CommonResponse;

import lombok.Data;

@Data
public class InsertRetroContractsReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("NoInsurer")
	private String noInsurer;
	@JsonProperty("RetentionPercentage")
	private String retentionPercentage;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("RetroDupContract")
	private String retroDupContract;
	@JsonProperty("RetroDupYerar")
	private String retroDupYerar;
	@JsonProperty("RetroType")
	private String retroType;
	@JsonProperty("RetroDetail")
	private List<RetroDetailReq> retroDetailReq;
}
