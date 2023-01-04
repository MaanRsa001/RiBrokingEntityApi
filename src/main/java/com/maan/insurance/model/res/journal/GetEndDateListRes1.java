package com.maan.insurance.model.res.journal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class GetEndDateListRes1 {
	@JsonProperty("ToDate")
	private String toDate;
	@JsonProperty("EndDate")
	private String endDate;
}
