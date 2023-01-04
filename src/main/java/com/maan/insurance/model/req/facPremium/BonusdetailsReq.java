package com.maan.insurance.model.req.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class BonusdetailsReq {
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("Layerno")
	private String layerno;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("Transaction")
	private String transaction;
	
	
	@JsonProperty("Mode")
	private String mode;
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("CountryId")
	private String countryId;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("ExchRate")
	private String exchRate;
	@JsonProperty("PremiumOC")
	private String premiumOC;
	
	@JsonProperty("CurrencyName")
	private String currencyName;
//	@JsonProperty("GridShow")
//	private String gridShow;

}
