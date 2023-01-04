package com.maan.insurance.model.res.home;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;

import lombok.Data;

@Data
public class GetFinalMenuListRes1 {
	@JsonProperty("MenuId")
	private String menuId;
	
	@JsonProperty("MenuName")
	private String menuName;
	
	@JsonProperty("MenuUrl")
	private String msenuUrl;
}
