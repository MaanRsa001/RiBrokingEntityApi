package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




/**
* Domain class for entity "TmasProductMaster"
*
* @author Telosys Tools Generator
*
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="TTRN_RIP")
public class TtrnRip implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="PROPOSAL_NO")
	    private String proposalNo;
	    
	    @Column(name="CONTRACT_NO")
	    private String contractNo;
	    
	    @Column(name="LAYER_NO")
	    private BigDecimal layerNo;
	    
	    @Column(name="AMEND_ID")
	    private BigDecimal amendId;
	    
	    @Column(name="REINST_NO")
	    private BigDecimal reinstNo;
	    
	    @Column(name="REINST_TYPE")
	    private String reinstType;
	    
	    @Column(name="AMOUNT_PERCENT")
	    private String amountPercent;
	    
	    @Column(name="MIN_AMOUNT_PERCENT")
	    private String minAmountPercent;
	    
	    @Column(name="MIN_TIME_PERCENT")
	    private String minTimePercent;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="SYS_DATE")
	    private Date sysDate;
	    
	    @Column(name="BRANCH_CODE")
	    private String  branchCode;
	    
	    @Column(name="SUB_CLASS")
	    private BigDecimal subClass;
	    
	    @Column(name="PRODUCT_ID")
	    private String poductId;
	    
	    @Column(name="REINSTATEMENT")
	    private String reinstatement;
	    
	    @Column(name="SECTION_TYPE")
	    private String sectionType;
	    
	    @Column(name="DEPARTMENT_CLASS")
	    private String departmentClass;
	    
	    @Column(name="ANNUAL_AGGRE_LAIBLE")
	    private BigDecimal annualAggreLaible;
	    
	    @Column(name="REFERENCE_NO")
	    private BigDecimal referenceNo; //Ri

}
