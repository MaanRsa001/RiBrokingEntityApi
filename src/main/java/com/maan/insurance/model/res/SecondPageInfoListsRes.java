package com.maan.insurance.model.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SecondPageInfoListsRes {
//	@JsonProperty("CedingCompanyValList")
//	private String cedingCompanyValList;
//	
//	@JsonProperty("ExachangeValList")
//	private String exachangeValList;
//	@JsonProperty("PayamountValList")
//	private String payamountValList;
//	@JsonProperty("RowamountValList")
//	private String rowamountValList;
//	@JsonProperty("RecNoValList")
//	private String recNoValList;
//	@JsonProperty("SetExcRateValList")
//	private String setExcRateValList;
//	@JsonProperty("ConRecCurValList")
//	private String conRecCurValList;
	@JsonProperty("listSecondPageInfo")
	private List<ListSecondPageInfo> listSecondPageInfo;
	@JsonProperty("Tottotal")
	private String tottotal;
	@JsonProperty("TotalConRecCur")
	private String totalConRecCur;
	@JsonProperty("Difftype")
	private String difftype;
	@JsonProperty("TxtDiffAmt")
	private String txtDiffAmt;
	@JsonProperty("TxtTotalAmt")
	private String txtTotalAmt;
	@JsonProperty("HideRowCnt")
	private String hideRowCnt;
	@JsonProperty("PaymentList")
	private List<String> paymentList;
	@JsonProperty("Difftype2")
	private String difftype2;
	
}