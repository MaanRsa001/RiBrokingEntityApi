package com.maan.insurance.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllTransContractReq {


	@JsonProperty("Alloccurrencyid")
	private String alloccurrencyid;

	@JsonProperty("Brokerid")
	private String brokerid;

	@JsonProperty("Cedingid")
	private String cedingid;
	
	@JsonProperty("BranchCode")
	private String branchCode;

	
}
