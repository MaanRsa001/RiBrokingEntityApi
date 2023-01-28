package com.maan.insurance.model.req.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReverseInsertReq {

	
	@JsonProperty("SerialNo")
    private String serialNo ;
	
	@JsonProperty("PayRecNo")
    private String payRecNo ;
	
	@JsonProperty("ReversalDate")
    private String reversalDate ;
	
	@JsonProperty("LoginId")
    private String loginId ;
	
	@JsonProperty("TransType")
    private String transType ;
	
	@JsonProperty("RetroType")
    private String retroType ;
	
	@JsonProperty("AllTillDate")
    private String allTillDate ;
	
	@JsonProperty("BranchCode")
    private String branchCode ;
	
	@JsonProperty("PaidAmount")
    private String paidAmount ;
	
	@JsonProperty("RetroPaidAmount")
    private String retroPaidAmount ;
	
	@JsonProperty("ContractNo")
    private String contractNo ;
	
	@JsonProperty("TransactionNo")
    private String transactionNo ;
	
	@JsonProperty("AllocatedDate")
    private String allocatedDate ;


}
