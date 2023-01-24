package com.maan.insurance.model.res.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetTransContractRes1Ri {
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("Mode")
	private String mode;
	
	@JsonProperty("ProductName")
	private String productName;
	
	@JsonProperty("TransactionNo")
	private String transactionNo;
	
	@JsonProperty("InceptiobDate")
	private String inceptiobDate;
	
	@JsonProperty("NetDue")
	private String netDue;
	
	@JsonProperty("Count")
	private String count;
	
	@JsonProperty("PayAmount")
	private String payAmount;
	
//	@JsonProperty("PayAmounts")
//	private String payAmounts;
	
	@JsonProperty("AccPremium")
	private String accPremium;
	
	@JsonProperty("AccClaim")
	private String accClaim;
	
	@JsonProperty("CheckYN")
	private String checkYN;
	
	@JsonProperty("CheckPC")
	private String checkPC;
	
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	
	@JsonProperty("AllocType")
	private String allocType;

	@JsonProperty("TotalRecCount")
	private String totalRecCount;
	
//	@JsonProperty("ReceivePayAmounts")
//	private String receivePayAmounts;
//
//	@JsonProperty("ChkBox")
//	private String chkBox;
	
//	@JsonProperty("PreviousValue")
//	private String previousValue;
	
	@JsonProperty("Transactionno")
    private String transactionno;
	
	@JsonProperty("SubClass")
    private String subClass;
	
	@JsonProperty("ProposalNo")
    private String proposalNo;

}
