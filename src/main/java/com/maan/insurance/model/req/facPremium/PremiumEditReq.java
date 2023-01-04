package com.maan.insurance.model.req.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.facPremium.GetDepartmentIdRes;
import com.maan.insurance.model.res.facPremium.GetDepartmentIdRes1;

import lombok.Data;

@Data
public class PremiumEditReq {
	@JsonProperty("Mode")
	private String mode;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("TransDropDownVal")
	private String transDropDownVal;
	@JsonProperty("Transaction")
	private String transaction;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("CountryId")
	private String countryId;
	@JsonProperty("TableType")
	private String tableType;
	@JsonProperty("RequestNo")
	private String requestNo;
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("RdsExchageRate")
	private String 	rdsExchageRate;
	@JsonProperty("GwpiOS")
	private String gwpiOS;
	@JsonProperty("PremiumQuotaShare")
	private String premiumQuotaShare;
	
}
