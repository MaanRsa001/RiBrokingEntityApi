package com.maan.insurance.model.req.statistics;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.adjustment.AdjustmentViewRes;
import com.maan.insurance.model.res.adjustment.AdjustmentViewResponse;

import lombok.Data;

@Data
public class GetRenewalStatisticsListReq {
	@JsonProperty("PeriodFrom")
	private String periodFrom;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("PeriodTo")
	private String periodTo;
	@JsonProperty("UwFrom")
	private String uwFrom;
	@JsonProperty("UwTo")
	private String uwTo;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("BranchCode")
	private String branchCode;

}
