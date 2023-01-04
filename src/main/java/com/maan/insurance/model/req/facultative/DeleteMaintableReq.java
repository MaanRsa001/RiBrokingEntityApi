package com.maan.insurance.model.req.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.nonproportionality.BonusReq;

import lombok.Data;

@Data
public class DeleteMaintableReq {
	@JsonProperty("Endorsmentno")
	private String endorsmentno;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("AcqBonus")
	private String acqBonus;

}
