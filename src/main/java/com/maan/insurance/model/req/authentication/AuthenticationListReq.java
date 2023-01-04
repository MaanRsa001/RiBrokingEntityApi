package com.maan.insurance.model.req.authentication;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.adjustment.AdjustmentViewRes;
import com.maan.insurance.model.res.adjustment.AdjustmentViewResponse;

import lombok.Data;

@Data
public class AuthenticationListReq {
	@JsonProperty("UploadStatus")
	private String uploadStatus;
	@JsonProperty("CheckItem")
	private String checkItem;
	@JsonProperty("BranchCode")
	private String branchCode;

}
