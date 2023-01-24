package com.maan.insurance.model.req.claim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.claim.CedentNoListRes;
import com.maan.insurance.model.res.claim.CedentNoListRes1;

import lombok.Data;

@Data
public class GetPaymentNoListReq {
	@JsonProperty("Contarctno")
	private String contarctno;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("Currecny")
	private String currecny;
}
