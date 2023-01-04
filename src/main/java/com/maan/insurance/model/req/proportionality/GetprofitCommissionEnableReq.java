package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.GetprofitCommissionEnableRes;

import lombok.Data;

@Data
public class GetprofitCommissionEnableReq {
	@JsonProperty("BaseLayer")
	private String baseLayer;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("Type")
	private String type;

}
