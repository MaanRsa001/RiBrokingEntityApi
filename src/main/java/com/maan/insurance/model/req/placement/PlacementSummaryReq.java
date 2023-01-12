package com.maan.insurance.model.req.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.DropDown.GetBouquetExistingListRes;
import com.maan.insurance.model.res.DropDown.GetBouquetExistingListRes1;

import lombok.Data;

@Data
public class PlacementSummaryReq {
	@JsonProperty("UwYearToSearch")
	private String uwYearToSearch;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("SearchType")
	private String searchType;
	@JsonProperty("CompanyNameSearch")
	private String companyNameSearch;
	@JsonProperty("BrokerNameSearch")
	private String brokerNameSearch; 
	@JsonProperty("UwYearSearch")
	private String uwYearSearch;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("IncepDateSearch")
	private String incepDateSearch; 
	@JsonProperty("ExpDateSearch")
	private String expDateSearch;
	
	
}
