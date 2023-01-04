package com.maan.insurance.model.req.adjustment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.adjustment.GetAdjustmentDoneListRes1;

import lombok.Data;

@Data
public class AdjustmentViewReq {
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("Mode")
	private String mode;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("BranchCode")
	private String branchCode;
}
