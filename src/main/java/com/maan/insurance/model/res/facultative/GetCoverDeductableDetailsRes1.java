package com.maan.insurance.model.res.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;

import lombok.Data;
@Data
public class GetCoverDeductableDetailsRes1 {
	@JsonProperty("CoverdepartId")
	private String coverdepartId;
	@JsonProperty("CoversubdepartId")
	private String coversubdepartId;
	@JsonProperty("CoverTypeId")
	private String coverTypeId;
	@JsonProperty("CoverLimitOC")
	private String coverLimitOC;
	@JsonProperty("DeductableLimitOC")
	private String deductableLimitOC;
	@JsonProperty("CoverageDays")
	private String coverageDays;
	@JsonProperty("DeductableDays")
	private String deductableDays;
	@JsonProperty("PremiumRateList")
	private String premiumRateList;
	@JsonProperty("PmlPerList")
	private String	pmlPerList;
	@JsonProperty("PmlHundredPer")
	private String	pmlHundredPer;
	@JsonProperty("EgnpiAsPerOff")
	private String egnpiAsPerOff;
	@JsonProperty("CoverRemark")
	private String	coverRemark;
	
	@JsonProperty("CoversubdeptList")
	private List<CommonResDropDown> coversubdeptList;
	
	@JsonProperty("Loopcount")
	private String 	loopcount;

}
