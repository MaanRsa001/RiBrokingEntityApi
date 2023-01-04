package com.maan.insurance.model.res.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.proportionality.CommonSaveRes;

import lombok.Data;

@Data
public class ViewPremiumDetailsRIReq {
	@JsonProperty("ReinsurerId")
	private String reinsurerId;
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ContractNo")
	private String contractNo;
//
//	@JsonProperty("BrokerId")
//	private String brokerId;


}
