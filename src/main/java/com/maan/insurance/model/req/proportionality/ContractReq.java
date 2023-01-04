package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractReq {
	@JsonProperty("CedingCo")
	private String cedingCo;
	@JsonProperty("IncepDate")
	private String incepDate;
	@JsonProperty("ExpDate")
	private String expDate;
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("OrginalCurrency")
	private String orginalCurrency;
	@JsonProperty("DepartId")
	private String departId;
	@JsonProperty("TreatyType")
	private String treatyType;
	@JsonProperty("ProfitCenter")
	private String profitCenter;
	@JsonProperty("FaclimitOrigCur")
	private String faclimitOrigCur;
	@JsonProperty("LimitOrigCur")
	private String limitOrigCur;
	@JsonProperty("TreatyLimitsurplusOC")
	private String treatyLimitsurplusOC;
	@JsonProperty("ContractTypelist")
	private String contractTypelist;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ContractListVal")
	private String contractListVal;
	@JsonProperty("LayerNo")
	private String layerNo;
}
