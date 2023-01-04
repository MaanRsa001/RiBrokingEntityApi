package com.maan.insurance.jpa.entity.facultative;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="TMAS_RISK_GRADE")
public class TmasRiskGrade {
	

	@Id
	@Column(name = "GRADE_ID")
	private Long gradeId;
	
	@Column(name = "GRADE_DESC")
	private String gradeDesc;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "CORE_APP_CODE")
	private String coreAppCode;
	
	@Column(name = "BRANCH_CODE")
	private String branchCode;
   
}
