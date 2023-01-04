package com.maan.insurance.jpa.entity.facultative;

import java.math.BigDecimal;
import java.sql.Date;

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
@Table(name="TTRN_FAC_SI")
public class TtrnFacSi {
	

	@Id
	@Column(name = "PROPOSAL_NO")
	private BigDecimal proposalNo;
	  
   @Column(name = "CONTRACT_NO")
   private BigDecimal contractNo;
   
   @Column(name = "LAYER_NO")
   private BigDecimal layerNo;
   
   @Column(name = "DEPT_ID")
   private BigDecimal deptId;
   
   @Column(name = "PRODUCT_ID")
   private BigDecimal productId;
   
   @Column(name = "AMEND_ID")
   private BigDecimal amendId;
   
   @Column(name = "RSK_SNO")
   private BigDecimal rskSno;
   
   @Column(name = "RSK_CLASS")
   private String rskClass;
   
   @Column(name = "RSK_SUBCLASS")
   private String rskSubclass;
   
   @Column(name = "RSK_TYPE")
   private String rskType;
   
   @Column(name = "RSK_COVERLIMIT_OC")
   private BigDecimal rskCoverlimitOc;
   
   @Column(name = "RSK_DEDUCTABLELIMIT_OC")
   private BigDecimal rskDeductablelimitOc;
   
   @Column(name = "RSK_COVERAGE_DAYS")
   private BigDecimal rskCoverageDays;
   
   @Column(name = "RSK_DEDUCTABLE_DAYS")
   private BigDecimal rskDeductableDays;
   
   @Column(name = "RSK_PREMIUM_RATE")
   private BigDecimal rskPremiumRate;
   
   @Column(name = "RSK_GWPI_OC")
   private BigDecimal rskGwpiOc;
   
   @Column(name = "RSK_COVER_REMARKS")
   private String rskCoverRemarks;
   
   @Column(name = "LOGIN_ID")
   private BigDecimal loginId;
   
   @Column(name = "BRANCH_CODE")
   private BigDecimal branchCode;
   
   @Column(name = "SYS_DATE")
   private Date sysDate;
   
   @Column(name = "PML_PERCENTAGE")
   private BigDecimal pmlPercentage;
   
   @Column(name = "PML_HUN_PER_OC")
   private BigDecimal pmlHunPerOc;
   
   @Column(name = "RSK_BUSINESS_TYPE")
   private String rskBusinessType;
   
   
}
