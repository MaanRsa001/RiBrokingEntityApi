package com.maan.insurance.model.res.reports;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.retro.FirstInsertRes;
import com.maan.insurance.model.res.retro.FirstInsertRes1;

import lombok.Data;

@Data
public class GetMoveMentInitRes1 {
	@JsonProperty("MovmentTranid")
	private String movmentTranid;
	@JsonProperty("DetailName")
	private String detailName;
	@JsonProperty("AccountPeriodQtr")
	private String accountPeriodQtr;
	@JsonProperty("AccountPeriodDate")
	private String accountPeriodDate;
	@JsonProperty("AccountPeriod")
	private String accountPeriod;
	@JsonProperty("ReportType")
	private String reportType;
	@JsonProperty("ProductName")
	private String productName;
	@JsonProperty("DeptName")
	private String deptName;
}
