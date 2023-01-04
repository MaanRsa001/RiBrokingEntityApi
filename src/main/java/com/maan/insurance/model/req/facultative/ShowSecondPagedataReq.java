package com.maan.insurance.model.req.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.GetClassLimitDetailsRes;
import com.maan.insurance.model.res.nonproportionality.GetClassLimitDetailsRes1;

import lombok.Data;

@Data
public class ShowSecondPagedataReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("NoOfInst")
	private String noOfInst;
	
	@JsonProperty("NoInsurer")
	private String noInsurer;
	
	@JsonProperty("PaymentDueDays")
	private List<String> 	paymentDueDays;
	@JsonProperty("ReceiptofPayment")
	private List<String> receiptofPayment;
	@JsonProperty("RetrolList")
	private List<String> retrolList;

	
	@JsonProperty("IncepDate")
	private String incepDate;

	@JsonProperty("RetroType")
	private String retroType;
	
//	@JsonProperty("Flag")
//	private String  flag;
	
	@JsonProperty("Year")
	private String  year;
}
