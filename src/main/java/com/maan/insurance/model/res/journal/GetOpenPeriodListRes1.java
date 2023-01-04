package com.maan.insurance.model.res.journal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class GetOpenPeriodListRes1 {
	@JsonProperty("SNO")
	private String sNo;
	@JsonProperty("OpenPeriodValue")
	private String openPeriodValue;
	@JsonProperty("OpenPeriodKey")
	private String openPeriodKey;
}
