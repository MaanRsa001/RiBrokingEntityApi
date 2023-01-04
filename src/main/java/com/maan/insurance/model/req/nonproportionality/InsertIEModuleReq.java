package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.GetInclusionExListRes;
import com.maan.insurance.model.res.nonproportionality.GetInclusionExListRes1;

import lombok.Data;

@Data
public class InsertIEModuleReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("EffectiveDate")
	private String effectiveDate;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("ExcludeProposalNo")
	private String excludeProposalNo;
	
	@JsonProperty("ExcludedList")
	private List<ExcludedListReq> excludedList;
	@JsonProperty("IncludedList")
	private List<IncludedListReq> includedList;
//	@JsonProperty("ExcludeProposalNoList")
//	private List<ExcludeProposalNoList> excludeProposalNo;
}
