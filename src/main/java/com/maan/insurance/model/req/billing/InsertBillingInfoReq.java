package com.maan.insurance.model.req.billing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.GetTransContractListReq;
import lombok.Data;

@Data
public class InsertBillingInfoReq {
	@JsonProperty("BillingNo")
	private String billingNo;
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
	@JsonProperty("GetTransContractListReq")
	private List<GetTransContractListReq> transContractListReq;

	/*
	 * @JsonProperty("Reversaltransno") private String reversaltransno;
	 * 
	 * @JsonProperty("Reverselloginid") private String reverselloginid;
	 * 
	 * @JsonProperty("Revtransaltype") private String revtransaltype;
	 * 
	 * @JsonProperty("Transcationtype") private String transcationtype;
	 * 
	 * @JsonProperty("Reversaldate") private String reversaldate;
	 */
}
