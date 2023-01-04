package com.maan.insurance.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.GetTreasuryJournalViewRes;

import lombok.Data;

@Data
public class GetTreasuryJournalViewReq {
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("SerialNo")
	private String serialNo;
	
	@JsonProperty("AllocationNo")
	private String allocationNo;
}
