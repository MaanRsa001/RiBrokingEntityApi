package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.ShowRetroContractsRes;
import com.maan.insurance.model.res.nonproportionality.ShowRetroContractsRes1;

import lombok.Data;

@Data
public class ShowRetroContractsReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("View")
	private Boolean view;
	
	@JsonProperty("AmendId")
	private String amendId;

	@JsonProperty("NoInsurer")
	private String noInsurer;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("IncepDate")
	private String incepDate;
	@JsonProperty("BranchCode")
	private String branchCode;
}
