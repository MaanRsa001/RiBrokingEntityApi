package com.maan.insurance.model.res.DropDown;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetContractValidationRes {
	@JsonProperty("ContractNo")
	private String ContractNo;
	
	@JsonProperty("EndoresmentNo")
	private String endoresmentNo;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("BrokerId")
	private String brokerId;
	
	@JsonProperty("UnderWritter")
	private String underWritter;
	
	@JsonProperty("InsuredName")
	private String insuredName;

}
