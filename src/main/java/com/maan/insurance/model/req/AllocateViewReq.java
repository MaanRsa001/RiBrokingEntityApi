package com.maan.insurance.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllocateViewReq {
	
	@JsonProperty("PayRecNo")
	private String payRecNo;
	
	@JsonProperty("SerialNo")
	private String serialNo;
	
	@JsonProperty("Flag")
	private String flag;
	
	@JsonProperty("AlloccurrencyId")
	private String alloccurrencyId;

	@JsonProperty("CurrecncyValue")
	private String currecncyValue;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
}

