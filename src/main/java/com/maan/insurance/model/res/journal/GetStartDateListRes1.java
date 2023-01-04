package com.maan.insurance.model.res.journal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class GetStartDateListRes1 {
	@JsonProperty("FromDate")
	private String fromDate;
	@JsonProperty("StartDate")
	private String startDate;
}
