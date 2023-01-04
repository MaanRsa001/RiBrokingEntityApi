package com.maan.insurance.model.res.reports;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class GetInwardRetroMappingReportRes1 {
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("RetroContractNo")
	private String retroContractNo;
	@JsonProperty("RetroLayerNo")
	private String retroLayerNo;
	@JsonProperty("RetroPercentage")
	private String retroPercentage;
	@JsonProperty("OwiPremiumUsd")
	private String owiPremiumUsd;
	@JsonProperty("OwiCommissionQs")
	private String owiCommissionQs;
	@JsonProperty("OwiCommissionSurplus")
	private String owiCommissionSurplus;
	@JsonProperty("OwiOtherCharges")
	private String owiOtherCharges;
	@JsonProperty("OverRider")
	private String overRider;
	@JsonProperty("Net")
	private String net;
	@JsonProperty("AccountDate")
	private String accountDate;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("CedingCompanyId")
	private String cedingCompanyId;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("OverRiderPer")
	private String overRiderPer;
	@JsonProperty("DeptId")
	private String deptId;
	@JsonProperty("OwiCommission")
	private String owiCommission;
}
