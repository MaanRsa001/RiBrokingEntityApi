package com.maan.insurance.model.req.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.facPremium.GetPremiumDetailsRes1;
import com.maan.insurance.model.res.facPremium.SettlementstatusRes;

import lombok.Data;

@Data
public class PremiumContractDetailsReq {
	@JsonProperty("Layerno")
	private String Layerno;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	

}
