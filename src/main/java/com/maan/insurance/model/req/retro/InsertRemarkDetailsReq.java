package com.maan.insurance.model.req.retro;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.nonproportionality.RemarksReq;
import com.maan.insurance.model.res.retro.CommonResponse;

import lombok.Data;

@Data
public class InsertRemarkDetailsReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("RemarksList")
	private List<RemarksReq> remarksReq;
}
