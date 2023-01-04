package com.maan.insurance.model.res.journal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetjounalListRes1 {
	@JsonProperty("CategoryDetailId")
	private String categoryDetailId;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("DetailName")
	private String detailName;
	@JsonProperty("Percent")
	private String percent;
	@JsonProperty("Param1")
	private String param1;
}
