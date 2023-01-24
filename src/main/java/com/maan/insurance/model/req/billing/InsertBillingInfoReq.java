package com.maan.insurance.model.req.billing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.claim.CedentNoListRes;
import com.maan.insurance.model.res.claim.CedentNoListRes1;

import lombok.Data;

@Data
public class InsertBillingInfoReq {
	@JsonProperty("BillingNo")
	private String billingNo;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("BillingSlNo")
	private String billingSlNo;
	@JsonProperty("AllocatedTillDate")
	private String allocatedTillDate;
	@JsonProperty("Amount")
	private String amount; 
	@JsonProperty("ConvertedReccur")
	private String convertedReccur;
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("ExchangeRate")
	private String exchangeRate;  
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("SettledExcrate")
	private String settledExcrate; 
	@JsonProperty("Status")
	private String status;
	@JsonProperty("TotAmt")
	private String totAmt; 
	@JsonProperty("TransDate")
	private String transDate; 
	
	//info
	@JsonProperty("TransType")
	private String transType;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("CedingId")
	private String cedingId; 
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("Reversaltransno")
	private String reversaltransno; 
	@JsonProperty("Reverselloginid")
	private String reverselloginid; 
	@JsonProperty("Revtransaltype")
	private String revtransaltype;
	@JsonProperty("Transcationtype")
	private String transcationtype; 
	@JsonProperty("AmendmentDate")
	private String amendmentDate;
	@JsonProperty("BillDate")
	private String billDate; 
	@JsonProperty("Reversaldate")
	private String reversaldate; 
}
