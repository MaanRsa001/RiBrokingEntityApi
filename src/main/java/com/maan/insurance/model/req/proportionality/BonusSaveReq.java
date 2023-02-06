package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BonusSaveReq {

	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("SlideScaleCommission")
	private String slideScaleCommission;
	@JsonProperty("LossParticipants")
	private String lossParticipants;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("Endorsmentno")
	private String endorsmentno;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("Productid")
	private String productid;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("DepartmentId")
	private String departmentId; 
	@JsonProperty("ReferenceNo")
	private String referenceNo;
	@JsonProperty("PageFor")
	private String pageFor;
	@JsonProperty("Sno")
	private String sno;
	
	
	@JsonProperty("ScaleMaxPartPercent")	
	private String scaleMaxPartPercent;
	@JsonProperty("FpcType")	
	private String fpcType;
	
	@JsonProperty("FpcfixedDate")	
	private String fpcfixedDate;

}
