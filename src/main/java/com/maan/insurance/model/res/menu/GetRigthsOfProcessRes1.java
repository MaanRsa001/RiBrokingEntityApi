package com.maan.insurance.model.res.menu;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetRigthsOfProcessRes1 {
	
	@JsonProperty("SubMenuCode")
	private String subMenuCode;
	
	@JsonProperty("SubMenuName")
	private String subMenuName;
}
