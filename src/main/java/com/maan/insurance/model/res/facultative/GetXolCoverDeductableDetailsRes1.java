package com.maan.insurance.model.res.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;

import lombok.Data;

@Data
public class GetXolCoverDeductableDetailsRes1 {
	@JsonProperty("XolcoverdepartId")
	private String xolcoverdepartId;
	
	@JsonProperty("XolcoversubdepartId")
	private String xolcoversubdepartId;
	
	@JsonProperty("XolcoverLimitOC")
	private String xolcoverLimitOC;
	
	@JsonProperty("XoldeductableLimitOC")
	private String xoldeductableLimitOC;
	
	@JsonProperty("XolpremiumRateList")
	private String xolpremiumRateList;
	
	@JsonProperty("XolgwpiOC")
	private String xolgwpiOC;
	
	@JsonProperty("CoversubdeptList")
	private List<CommonResDropDown> coversubdeptList; 
	
	@JsonProperty("XolLoopcount")
	private String xolLoopcount;
}
