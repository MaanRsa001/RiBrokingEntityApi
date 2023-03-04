package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.claim.ContractidetifierlistReq;

import lombok.Data;

@Data
public class ContractidetifierlistRes {
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
	
	@JsonProperty("NewLayerNo")
	private String newLayerNo;
}
