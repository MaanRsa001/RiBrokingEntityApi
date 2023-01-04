package com.maan.insurance.model.req;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetRetroallocateTransactionReq {
	
	
	@JsonProperty("AccountDate")
	private String  AccountDate;

	@JsonProperty("SerialNo")
	private String  serialNo;

	@JsonProperty("PayRecNo")
	private String  payRecNo;

	@JsonProperty("PolicyNo")
	private String  policyNo;

	@JsonProperty("TransType")
    private String transType ;
	
	@JsonProperty("AllOccurencyId")

    private String allOccurencyId ;
	
	@JsonProperty("BrokerId")
    private String brokerId ;
	
	@JsonProperty("LoginId")
    private String loginId ;
	
	@JsonProperty("CedingId")
    private String cedingId ;
	
	@JsonProperty("BranchCode")
    private String branchCode ;
	
	@JsonProperty("HideprocessType")
    private String hideprocessType;
	
	@JsonProperty("SubClass")
    private String subClass ;
	
	@JsonProperty("ProposalNo")
    private String proposalNo ;
	
	@JsonProperty("AmendDate")
    private String amendDate ;
	
	@JsonProperty("ContractNo")
    private String contractNo ;
	
	@JsonProperty("productName")
	private String ProductName;
	
	
	@JsonProperty("GetTransContractListReq")
	private List<GetTransContractListReq> transContractListReq;

	private String  alOccurencyId;


	
}
