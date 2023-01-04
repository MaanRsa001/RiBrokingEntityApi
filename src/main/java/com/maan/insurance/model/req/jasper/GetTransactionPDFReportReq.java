package com.maan.insurance.model.req.jasper;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetTransactionPDFReportReq {
	@JsonProperty("ImagePath")
	public String imagePath;
	
	@JsonProperty("SysDate")
	public String sysDate;
	
	@JsonProperty("ReceiptNo")
	public String receiptNo;
	
	@JsonProperty("BranchCode")
	public String branchCode;
	
}
