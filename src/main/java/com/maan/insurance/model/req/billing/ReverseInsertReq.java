package com.maan.insurance.model.req.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReverseInsertReq {

	
	@JsonProperty("BillingNo")
    private String billingNo ;
	
	@JsonProperty("BillDeleteDate")
    private String reversalDate ;
	
	@JsonProperty("LoginId")
    private String loginId ;
	
	@JsonProperty("BillingType")
    private String transType ;
	
	@JsonProperty("BranchCode")
    private String branchCode ;
	
	@JsonProperty("RoundingAmount")
    private String roundingAmount ;
	
	@JsonProperty("BillAmount")
    private String utilizedTillDate ;
	
	
}
