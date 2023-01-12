package com.maan.insurance.model.res.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertDocdetailsRes {
	@JsonProperty("FileName")
	private String fileName; 
}
