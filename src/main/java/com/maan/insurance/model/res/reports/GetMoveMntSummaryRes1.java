package com.maan.insurance.model.res.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetMoveMntSummaryRes1 {
	@JsonProperty("Rownum")
	private String rownum;
	@JsonProperty("TmasSpfcName")
	private String tmasSpfcName;
	@JsonProperty("RskUwyear")
	private String rskUwyear;
	@JsonProperty("sumoftotprDc")
	private String sumoftotprDc;
}
