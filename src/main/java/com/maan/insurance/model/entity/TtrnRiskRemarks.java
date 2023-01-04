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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@DynamicInsert
@DynamicUpdate
@Builder
@Table(name="TTRN_RISK_REMARKS")
public class TtrnRiskRemarks implements Serializable{
	private static final long serialVersionUID = 1L;
	 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="PROPOSAL_NO", nullable=false)
    private BigDecimal proposalNo ;
    
    //--- ENTITY DATA FIELDS 
    @Column(name="CONTRACT_NO")
    private BigDecimal contractNo ;

    @Column(name="LAYER_NO")
    private BigDecimal layerNo ;


    @Column(name="DEPT_ID", length=20)
    private String     deptId ;

    @Column(name="PRODUCT_ID", length=20)
    private String     productId ;

    @Column(name="RSK_SNO")
    private String     rskSNo ;

    @Column(name="RSK_REMARK1")
    private String     rskRemark1 ;
    
    @Column(name="RSK_REMARK2")
    private String     rskRemark2 ;
    
    @Column(name="LOGIN_ID")
    private String     loginId ;
    
    @Column(name="BRANCH_CODE")
    private String     branchCode ;
    
    @Column(name="RSK_DESCRIPTION")
    private String     rskDescription ;
    
    @Column(name="AMEND_ID")
    private String     amendId ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="SYS_DATE")
    private Date    sysDate ;
}

