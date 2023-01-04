package com.maan.insurance.model.req.statistics;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.statistics.GetCurrencyNameRes;
import com.maan.insurance.model.res.statistics.GetCurrencyNameRes1;

import lombok.Data;

@Data
public class GetColumnHeaderlistReq {
	@JsonProperty("PeriodFrom")
	private String periodFrom;
	@JsonProperty("PeriodTo")
	private String periodTo;
}
