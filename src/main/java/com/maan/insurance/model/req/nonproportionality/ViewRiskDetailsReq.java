package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.InstalmentListRes;
import com.maan.insurance.model.res.proportionality.ViewRiskDetailsRes1;

import lombok.Data;

@Data
public class ViewRiskDetailsReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("AmendId")
	private String amendId;

	@JsonProperty("SubProfitCenter")
	private String subProfitCenter;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("SharSign")
	private String sharSign;
	
//	@JsonProperty("Territory")
//	private String territory;
	
//	@JsonProperty("PremiumQuotaShareOSOC")
//	private String premiumQuotaShareOSOC;
//	
//	@JsonProperty("PremiumSurplusOSOC")
//	private String premiumSurplusOSOC;
	
//	@JsonProperty("CountryIncludedName")
//	private String countryIncludedName;
//	@JsonProperty("CountryExcludedName")
//	private String countryExcludedName;
	@JsonProperty("ProductId")
	private String productId;
}
