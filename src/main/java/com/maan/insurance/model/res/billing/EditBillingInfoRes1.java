package com.maan.insurance.model.res.billing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.GetTransContractListReq;
import com.maan.insurance.model.req.billing.InsertBillingInfoReq;

import lombok.Data;

@Data
public class EditBillingInfoRes1 {
	
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("CurrencyId")
	private String currencyId; 
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("Remarks")
	private String remarks;
	
	//info
	@JsonProperty("TransType")
	private String transType;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("CedingId")
	private String cedingId; 
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("AmendmentDate")
	private String amendmentDate;
	@JsonProperty("BillDate")
	private String billDate; 
	
	@JsonProperty("AmedId")
	private String amedId; 
	@JsonProperty("ReversalDate")
	private String reversalDate;
	@JsonProperty("ReversalTransno")
	private String reversalTransno;
	@JsonProperty("ReverselLoginid")
	private String reverselLoginid; 
	@JsonProperty("RevTransalType")
	private String revTransalType;
	@JsonProperty("RoundingAmount")
	private String roundingAmount;
	@JsonProperty("status")
	private String status;
	@JsonProperty("TranscationType")
	private String transcationType;
	@JsonProperty("UtilizedTillDate")
	private String utilizedTillDate; 
}
