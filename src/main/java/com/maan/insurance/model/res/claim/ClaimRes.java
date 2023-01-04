package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimRes {
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("DateofLoss")
	private String dateofLoss;
	@JsonProperty("CreatedDate")
	private String createdDate;
	@JsonProperty("Statusofclaim")
	private String statusofclaim;
	@JsonProperty("PolicyContractNo")
	private String policyContractNo;
	@JsonProperty("EditMode")
	private String editMode;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("CedingcompanyName")
	private String cedingcompanyName;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ProductName")
	private String productName;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("DeleteStatus")
	private String deleteStatus;
	
	
	
	

}
