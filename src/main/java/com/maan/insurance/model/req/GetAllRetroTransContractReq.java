package com.maan.insurance.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllRetroTransContractReq {

	@JsonProperty("TransType")
    private String transType ;
	
	@JsonProperty("AllOccurencyId")
    private String allOccurencyId ;
	
	@JsonProperty("BrokerId")
    private String brokerId ;
	
	@JsonProperty("CedingId")
    private String cedingId ;
	
	@JsonProperty("BranchCode")
    private String branchCode ;
}
