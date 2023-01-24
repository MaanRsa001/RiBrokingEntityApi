package com.maan.insurance.model.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTransContractReq {

	@JsonProperty("TransType")
	private String transType;
	
	@JsonProperty("AlloccurrencyId")
	private String alloccurrencyId;
	
	@JsonProperty("BrokerId")
	private String brokerId;
	
	@JsonProperty("CedingId")
	private String cedingId;
	
	@JsonProperty("AllocType")
	private String allocType;
	
	
	@JsonProperty("TotalRecCount")
	private String totalRecCount;
	
	
	@JsonProperty("PayAmounts")
	private String payAmounts;
	
	@JsonProperty("ChkBox")
	private String chkBox;
	
	@JsonProperty("BranchCode")
	private String branchCode;

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
