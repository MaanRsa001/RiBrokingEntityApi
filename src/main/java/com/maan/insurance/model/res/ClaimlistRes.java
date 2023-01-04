package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimlistRes {
	@JsonProperty("ClaimNo")
	private String claimNo;
	
	@JsonProperty("DateOfLoss")
	private String dateOfLoss;
	
	@JsonProperty("CreatedDate")
	private String createdDate;
	
	@JsonProperty("StatusOfClaim")
	private String statusOfClaim;

	@JsonProperty("PolicyContractNo")
	private String policyContractNo;
	
	@JsonProperty("EditMode")
	private String editMode;
	
	@JsonProperty("DeleteStatus")
	private String deleteStatus;
}
