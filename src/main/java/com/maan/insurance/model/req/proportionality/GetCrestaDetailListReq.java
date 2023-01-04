package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.RetroFinalListres;
import lombok.Data;

@Data
public class GetCrestaDetailListReq {
	@JsonProperty("Proposalno")
	private String proposalno;
	
	@JsonProperty("AmendId")
	private String amendId;

	@JsonProperty("BranchCode")
	private String branchCode;
}
