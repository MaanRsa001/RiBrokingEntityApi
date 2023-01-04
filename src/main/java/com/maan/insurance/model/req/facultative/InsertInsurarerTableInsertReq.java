package com.maan.insurance.model.req.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.nonproportionality.InstalmentperiodReq;

import lombok.Data;
@Data
public class InsertInsurarerTableInsertReq {
	@JsonProperty("ProposalNo")
	private String ProposalNo;

	@JsonProperty("NoInsurer")
	private String noInsurer;
	
	@JsonProperty("Retper")
	private String retper;
	
	@JsonProperty("Loginid")
	private String loginid;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("RetroDupContract")
	private String retroDupContract;
	
	@JsonProperty("RetroDupYerar")
	private String retroDupYerar;
	
	@JsonProperty("RetroDetails")
	private List<RetroDetails> retroDetails;

}
