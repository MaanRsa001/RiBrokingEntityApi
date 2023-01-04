package com.maan.insurance.model.req.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.nonproportionality.RemarksRes;

import lombok.Data;

@Data
public class ViewModeReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("BranchCode")
	private String branchCode;

}
