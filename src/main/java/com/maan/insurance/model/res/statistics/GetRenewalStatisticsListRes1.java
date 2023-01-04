package com.maan.insurance.model.res.statistics;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.adjustment.AdjustmentViewRes;
import com.maan.insurance.model.res.adjustment.AdjustmentViewResponse;

import lombok.Data;

@Data
public class GetRenewalStatisticsListRes1 {
	@JsonProperty("Item")
	private String item;
	@JsonProperty("Cnt")
	private String cnt;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("M1")
	private String m1;
	@JsonProperty("M2")
	private String m2;
	@JsonProperty("M3")
	private String m3;
	@JsonProperty("M4")
	private String m4;
	@JsonProperty("M5")
	private String m5;
	@JsonProperty("M6")
	private String m6;
	@JsonProperty("M7")
	private String m7;
	@JsonProperty("M8")
	private String m8;
	@JsonProperty("M9")
	private String m9;
	@JsonProperty("M10")
	private String m10;
	@JsonProperty("M11")
	private String m11;
	@JsonProperty("M12")
	private String m12;
	@JsonProperty("M13")
	private String m13;
	@JsonProperty("M14")
	private String m14;
	@JsonProperty("M15")
	private String m15;
	@JsonProperty("M16")
	private String m16;
	@JsonProperty("M17")
	private String m17;
	@JsonProperty("M18")
	private String m18;
	@JsonProperty("M19")
	private String m19;
	@JsonProperty("M20")
	private String m20;
	@JsonProperty("Total")
	private String total;
}
