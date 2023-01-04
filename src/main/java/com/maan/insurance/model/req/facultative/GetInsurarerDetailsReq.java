package com.maan.insurance.model.req.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageDataRes;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageDataRes1;

import lombok.Data;

@Data
public class GetInsurarerDetailsReq {
	@JsonProperty("NoInsurer")
	private String noInsurer;
	
	@JsonProperty("View")
	private String view;
	
	@JsonProperty("AmendId")
	private String amendId;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("Year")
	private String year;
	
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	
	@JsonProperty("BranchCode")
	private String branchCode;
}
