package com.maan.insurance.model.res.statistics;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetColumnHeaderlistRes1 {
	@JsonProperty("Range")
	private List<Map<String,Object>> range;
}
