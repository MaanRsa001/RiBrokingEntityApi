package com.maan.insurance.model.req.billing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.GetTransContractListReq;
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
	
	//getAllocateTransaction
	
	@JsonProperty("AlloccurrencyId")
	private String alloccurrencyId;
	
	
	@JsonProperty("AllocType")
	private String allocType;
	
	
	@JsonProperty("TotalRecCount")
	private String totalRecCount;
	
	
	@JsonProperty("PayAmounts")
	private String payAmounts;
	
	@JsonProperty("ChkBox")
	private String chkBox;
	

	@JsonProperty("AccountDate")
	private String accountDate;
	@JsonProperty("Serialno")
	private String serialno;
	@JsonProperty("Loginid")
	private String loginid;
	@JsonProperty("Policyno")
	private String policyno;
	@JsonProperty("Payrecno")
	private String payrecno;

	@JsonProperty("HideprocessType")
	private String hideprocessType;
	
	@JsonProperty("GetTransContractListReq")
	private List<GetTransContractListReq> transContractListReq;
	@JsonProperty("AmendDate")
	private String amendDate;
	
	@JsonProperty("CurrencyValue")
	private String currencyValue;
	
	
}
