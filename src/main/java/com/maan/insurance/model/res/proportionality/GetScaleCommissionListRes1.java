package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;

import lombok.Data;

@Data
public class GetScaleCommissionListRes1 {

	
	
	
	@JsonProperty("BonusTypeId")
	private String bonusTypeId;
	@JsonProperty("QuotaShare")
	private String quotaShare;
	@JsonProperty("Bonusremarks")
	private String bonusremarks;
	@JsonProperty("ScFistpc")
	private String scFistpc;
	@JsonProperty("ScProfitMont")
	private String scProfitMont;
	@JsonProperty("ScSubpc")
	private String scSubpc;
	@JsonProperty("ScSubProfitMonth")
	private String scSubProfitMonth;
	@JsonProperty("ScSubSeqCalculation")
	private String scSubSeqCalculation;
	@JsonProperty("FpcType") //Ri
	private String fpcType;
	@JsonProperty("FpcfixedDate")
	private String fpcfixedDate;
	@JsonProperty("ScaleMaxPartPercent")
	private String scaleMaxPartPercent;
	
	@JsonProperty("BonusSno")
	private String bonusSno;
	@JsonProperty("BonusFrom")
	private String bonusFrom;
	@JsonProperty("BonusTo")
	private String bonusTo;
	@JsonProperty("BonusLowClaimBonus")
	private String bonusLowClaimBonus;
//	@JsonProperty("CoversubdeptList")
//	private List<CommonResDropDown> coversubdeptList;
}
