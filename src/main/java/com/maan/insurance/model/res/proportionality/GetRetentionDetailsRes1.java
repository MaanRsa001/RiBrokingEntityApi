package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.ContractReq;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;

import lombok.Data;

@Data
public class GetRetentionDetailsRes1 {

	@JsonProperty("CoverdepartId")
	private String coverdepartId;
	@JsonProperty("CoversubdepartId")
	private String coversubdepartId;
	@JsonProperty("RetType")
	private String retType;
	@JsonProperty("RetBasis")
	private String retBasis;
	@JsonProperty("RetBusinessType")
	private String retBusinessType;
	@JsonProperty("Firstretention")
	private String firstretention;
	@JsonProperty("Secondretention")
	private String secondretention;
	@JsonProperty("RetTreatyFST")
	private String retTreatyFST;

	@JsonProperty("RetTreatySST")
	private String retTreatySST;
	@JsonProperty("RetEventFST")
	private String retEventFST;
	@JsonProperty("retEventSST")
	private String RetEventSST;
	@JsonProperty("CoversubdeptList")
	private List<CommonResDropDown> coversubdeptList;
}
