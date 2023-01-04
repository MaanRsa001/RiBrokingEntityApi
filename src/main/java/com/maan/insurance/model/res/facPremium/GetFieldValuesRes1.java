package com.maan.insurance.model.res.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.facPremium.GetBonusValueReq;
import com.maan.insurance.model.req.facPremium.PreCurrencylist;

import lombok.Data;

@Data
public class GetFieldValuesRes1 {
//	@JsonProperty("PreCurrencylist")
//	private List<PreCurrencylistRes> preCurrencylist; 
//	@JsonProperty("PremList")
//	private List<PremListRes> premList;
	
	@JsonProperty("PremiumOC")
	private List<String> premiumOC;
	@JsonProperty("ClaimPaidOC")
	private List<String> claimPaidOC;
	@JsonProperty("ClaimRatioOC")
	private List<String> claimRatioOC;
	@JsonProperty("ClaimOutStandingOC")
	private List<String> claimOutStandingOC;
	@JsonProperty("BonusOC")
	private List<String> bonusOC;
	@JsonProperty("BonusPaidOCTillDate")
	private List<String> bonusPaidOCTillDate;
	@JsonProperty("BonusAdjOC")
	private List<String> bonusAdjOC;

	@JsonProperty("ManualclaimOutStandingOC")
	private List<String> manualclaimOutStandingOC;
	@JsonProperty("ManualclaimRatioOC")
	private List<String> manualclaimRatioOC;
	@JsonProperty("ManualPremiumOC")
	private List<String> manualPremiumOC;
	@JsonProperty("ManualclaimPaidOC")
	private List<String> manualclaimPaidOC;
}
