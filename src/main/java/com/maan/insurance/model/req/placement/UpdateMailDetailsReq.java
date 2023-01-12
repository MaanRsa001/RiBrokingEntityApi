package com.maan.insurance.model.req.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.placement.GetPlacingInfoRes1;

import lombok.Data;

@Data
public class UpdateMailDetailsReq {
	@JsonProperty("Status")
	private String status;
	@JsonProperty("StatusNo")
	private String statusNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("MailType")
	private String mailType;
	@JsonProperty("UpdateMailDetailsReq")
	private List<UpdateMailDetailsReq1> updateMailDetailsReqList;
	
}
