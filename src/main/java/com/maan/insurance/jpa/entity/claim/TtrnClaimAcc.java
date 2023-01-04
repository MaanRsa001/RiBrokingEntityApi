package com.maan.insurance.jpa.entity.claim;

import java.io.Serializable;
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
@Table(name="TTRN_CLAIM_ACC")
public class TtrnClaimAcc implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    @Id
    @Column(name="CLAIM_NO")
    private Long claimNo;
    
    @Column(name="CONTRACT_NO")
    private String contractNo;
    
    @Column(name="LAYER_NO")
    private Integer layerNo;
    
    @Column(name="SUB_CLASS")
    private Integer subClass;
    
    @Column(name="RISK_CODE")
    private String riskCode;
    
    @Column(name="AGGREGATE_CODE")
    private String aggregateCode;
    
    @Column(name="EVENT_CODE")
    private String eventCode;
    
    @Column(name="AMEND_ID")
    private Integer amendId;
    
    @Column(name="AMEND_DATE")
    private Date amendDate;
    
    @Column(name="CREATED_DATE")
    private Date createdDate;
    
    @Column(name="BRANCH_CODE")
    private String  branchCode;
    
    @Column(name="LOGIN_ID")
    private Integer loginId;
    
}
