package com.maan.insurance.model.res.home;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetDepartmentListRes1 {
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("DepartmentName")
	private String departmentName;
}
