package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;

import lombok.Data;

@Data
public class GetScaleCommissionListRes1 {

	
	@JsonProperty("BonusDetails")
	private List<BonusDetailsRes> bonusDetails;
	
	
	@JsonProperty("BonusTypeId")
	private String bonusTypeId;
	@JsonProperty("QuotaShare")
	private String quotaShare;
	@JsonProperty("Bonusremarks")
	private String bonusremarks;
	@JsonProperty("Fistpc")
	private String fistpc;
	@JsonProperty("ProfitMont")
	private String profitMont;
	@JsonProperty("Subpc")
	private String subpc;
	@JsonProperty("SubProfitMonth")
	private String subProfitMonth;
	@JsonProperty("SubSeqCalculation")
	private String subSeqCalculation;
//	@JsonProperty("CoversubdeptList")
//	private List<CommonResDropDown> coversubdeptList;
}
