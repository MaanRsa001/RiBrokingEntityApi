package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetConstantPeriodDropDownReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("CategoryId")
	private String categoryId;
	@JsonProperty("SectionNo")
	private String sectionNo;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("ContractNo")
	private String contractNo;
}
