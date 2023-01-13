package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetMailTemplateReq {
	@JsonProperty("MailType")
	private String mailType;
	@JsonProperty("TranasctionNo")
	private String tranasctionNo; 
	@JsonProperty("BranchCode")
	private String branchCode;
	/*
	 * @JsonProperty("MailTo") private String mailTo;
	 * 
	 * @JsonProperty("MailCC") private String mailCC;
	 */
}
