package com.maan.insurance.model.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RetroTransReq {
	
	@JsonProperty("Alloccurrencyid")
	private String alloccurrencyid;
	@JsonProperty("Brokerid")
	private String brokerid;
	@JsonProperty("Cedingid")
	private String cedingid;
	@JsonProperty("AllocType")
	private String allocType;
	
	@JsonProperty("BranchCode")
	private String branchcode;
	@JsonProperty("TransType")
	private String transType;
	
	
	@JsonProperty("GetTransContractListReq")
	private List<GetTransContractListReq> transContractListReq;
	

}
