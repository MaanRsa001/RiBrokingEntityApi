package com.maan.insurance.model.req.portFolio;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.portFolio.GetRejectedListRes1;

import lombok.Data;

@Data
public class GetAutoPendingListReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("Flag")
	private String flag;
	@JsonProperty("DueDate")
	private String dueDate;
}
