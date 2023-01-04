package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.proportionality.GetCrestaDetailListRes1;

import lombok.Data;

@Data
public class InsertCrestaDetailsReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("AmendId")
	private String amendId;

	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("CrestaId")
	private List<CrestaReq> CrestaReq;
}
