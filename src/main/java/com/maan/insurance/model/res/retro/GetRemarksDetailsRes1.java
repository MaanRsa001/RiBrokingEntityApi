package com.maan.insurance.model.res.retro;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.nonproportionality.RemarksRes;

import lombok.Data;

@Data
public class GetRemarksDetailsRes1 {
	@JsonProperty("Result")
	private List<RemarksRes> remarksRes;
	
	@JsonProperty("RemarkCount")
	private String remarkCount;
}
