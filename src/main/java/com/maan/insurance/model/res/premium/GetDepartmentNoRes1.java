package com.maan.insurance.model.res.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetDepartmentNoRes1 {
	@JsonProperty("DeptNo")
	private String deptNo;
}
