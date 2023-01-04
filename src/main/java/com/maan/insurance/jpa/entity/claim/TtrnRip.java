package com.maan.insurance.jpa.entity.claim;

import java.io.Serializable;
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
    private Integer layerNo;
    
    @Column(name="AMEND_ID")
    private Integer amendId;
    
    @Column(name="REINST_NO")
    private Integer reinstNo;
    
    @Column(name="REINST_TYPE")
    private String reinstType;
    
    @Column(name="AMOUNT_PERCENT")
    private String amountPercent;
    
    @Column(name="MIN_AMOUNT_PERCENT")
    private String minAmountPercent;
    
    @Column(name="MIN_TIME_PERCENT")
    private String minTimePercent;
    
    @Column(name="SYS_DATE")
    private Date sysDate;
    
    @Column(name="BRANCH_CODE")
    private String  branchCode;
    
    @Column(name="SUB_CLASS")
    private Integer subClass;
    
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

}
