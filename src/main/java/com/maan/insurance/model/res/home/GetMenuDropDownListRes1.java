package com.maan.insurance.model.res.home;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.facultative.GetRetroContractDetailsListRes1;
import com.maan.insurance.model.res.facultative.GetRetroContractDetailsRes;

import lombok.Data;

@Data
public class GetMenuDropDownListRes1 {
	@JsonProperty("MenuKey")
	private String menuKey;
	
	@JsonProperty("MenuList")
	private String menuList;
}
