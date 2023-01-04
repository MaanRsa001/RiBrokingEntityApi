package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetRetDetailsRes1 {
	
	@JsonProperty("RetList")
	private List<RetListRes> retList;	
	@JsonProperty("Loopcount")
	private String loopcount;
}
