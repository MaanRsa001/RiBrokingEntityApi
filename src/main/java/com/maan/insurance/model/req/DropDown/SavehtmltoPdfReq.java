package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SavehtmltoPdfReq {
	
	@JsonProperty("TransactionNo")
	private String transactionNo;
	
	@JsonProperty("HtmlContent")
	private String htmlContent;


}
