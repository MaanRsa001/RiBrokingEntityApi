package com.maan.insurance.model.req.jasper;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetRetroReportReq {
	@JsonProperty("StartDate")
	public String startDate;
	
	@JsonProperty("EndDate")
	public String endDate;
	@JsonProperty("BranchCode")
	public String branchCode;
	
	@JsonProperty("Type")
	public String type; 

}
