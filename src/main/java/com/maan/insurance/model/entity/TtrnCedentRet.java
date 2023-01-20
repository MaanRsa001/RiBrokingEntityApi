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
@Table(name="TTRN_CEDENT_RET")
public class TtrnCedentRet implements Serializable{
	private static final long serialVersionUID = 1L;
	 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="PROPOSAL_NO", nullable=false)
    private String proposalNo ;
    
    //--- ENTITY DATA FIELDS 
    @Column(name="CONTRACT_NO")
    private String contractNo ;
    
    @Column(name="AMEND_ID")
    private String amendId ;
    
    @Column(name="BRANCH_CODE")
    private String     branchCode ;
    
    @Column(name="DEPT_ID")
    private String     deptId ;
    

    @Column(name="LAYER_NO")
    private BigDecimal layerNo ;

    @Column(name="LOGIN_ID")
    private String     loginId ;


    @Column(name="PRODUCT_ID")
    private String     productId ;

    @Column(name="RSK_BASISTYPE")
    private String     rskBasistype ;

    @Column(name="RSK_CLASS")
    private String     rskClass ;
    
    @Column(name="RSK_FIRST_RET_OC")
    private BigDecimal     rskFirstRetOc;
    
    @Column(name="RSK_RETTYPE")
    private String     rskRettype ;
    
    @Column(name="RSK_RET_EL_FST_OC")
    private BigDecimal     rskRetElFstOc ;
    
    @Column(name="RSK_RET_EL_SST_OC")
    private BigDecimal     rskRetElSstOc;
    
    @Column(name="RSK_RET_TL_FST_OC")
    private BigDecimal     rskRetTlFstOc ;
    
    @Column(name="RSK_RET_TL_SST_OC")
    private BigDecimal     rskRetTlSstOc ;
    
    @Column(name="RSK_SECOND_RET_OC")
    private BigDecimal     rskSecondRetOc ;
    
    @Column(name="RSK_SNO")
    private String     rskSno ;
    
    @Column(name="RSK_SUBCLASS")
    private String     rskSubclass ;
    
    @Column(name="RSK_TYPE")
    private String     rskType ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="SYS_DATE")
    private Date       sysDate ;

}
