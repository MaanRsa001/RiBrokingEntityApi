package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertReceiptNoRes {
	
	@JsonProperty("Flag")
	private boolean flag;
	
	@JsonProperty("SerialNo")
	private String serialNo;
}
