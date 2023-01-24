package com.maan.insurance.model.req.billing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.GetTransContractListReq;
import com.maan.insurance.model.res.billing.GetTransContractResRi;

import lombok.Data;

@Data
public class GetTransContractReqRi {

	
	@JsonProperty("AlloccurrencyId")
	private String alloccurrencyId;
	
	@JsonProperty("BrokerId")
	private String brokerId;
	
	@JsonProperty("CedingId")
	private String cedingId;
	
//	@JsonProperty("AllocType")
//	private String allocType;
//	
//	@JsonProperty("TransType")
//	private String transType;
//	@JsonProperty("TotalRecCount")
//	private String totalRecCount;
//	
//	
//	@JsonProperty("PayAmounts")
//	private String payAmounts;
//	
//	@JsonProperty("ChkBox")
//	private String chkBox;
	
	@JsonProperty("BranchCode")
	private String branchCode;

//	@JsonProperty("AccountDate")
//	private String accountDate;
//	@JsonProperty("Serialno")
//	private String serialno;
//	@JsonProperty("Loginid")
//	private String loginid;
//	@JsonProperty("Policyno")
//	private String policyno;
//	@JsonProperty("Payrecno")
//	private String payrecno;
//
//	@JsonProperty("HideprocessType")
//	private String hideprocessType;
//	
//	@JsonProperty("GetTransContractListReq")
//	private List<GetTransContractListReq> transContractListReq;
//	@JsonProperty("AmendDate")
//	private String amendDate;
//	
//	@JsonProperty("CurrencyValue")
//	private String currencyValue;
}
