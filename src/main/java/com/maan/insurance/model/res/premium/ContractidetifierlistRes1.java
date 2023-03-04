package com.maan.insurance.model.res.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractidetifierlistRes1 {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("CedingcompanyName")
	private String cedingcompanyName;
	
	@JsonProperty("BrokerName")
	private String brokerName;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("TransactionNumber")
	private String transactionNumber;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("DeptId")
	private String deptId;
	
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	
	@JsonProperty("TransactionDate")
	private String transactionDate;
	
	@JsonProperty("UnderwritingYear")
	private String underwritingYear;
	
	@JsonProperty("Underwriter")
	private String underwriter;
	
	@JsonProperty("OldContract")
	private String oldContract;
	
	@JsonProperty("ClaimNo")
	private String claimNo;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("ClaimCount")
	private String claimCount;  
	
	@JsonProperty("TransactionType")
	private String transactionType;
	
	@JsonProperty("NewLayerno")
	private String newLayerno;
}
