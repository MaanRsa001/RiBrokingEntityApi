package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
@IdClass(TtrnCommissionDetailsId.class)
@Table(name="TTRN_COMMISSION_DETAILS")
public class TtrnCommissionDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="BRANCH_CODE", nullable=false)
    private String     branchCode ;
    @Id
    @Column(name="SERIAL_NO", nullable=false)
    private BigDecimal     serialNo ;
    
    //--- ENTITY DATA FIELDS 
    @Column(name="REFERENCE_NO")
    private BigDecimal referenceNo ;
    
    @Column(name="PROPOSAL_NO")
    private BigDecimal proposalNo ;
    
    @Column(name="CONTRACT_NO")
    private BigDecimal contractNo ;
    
    @Column(name="SNO")
    private String     sNo ;
    
    @Column(name="COMM_FROM")
    private String     commFrom ;
    

    @Column(name="COMM_TO")
    private String commTo ;

    @Column(name="PROFIT_COMM")
    private String     profitComm ;


    @Column(name="ENDORSEMENT_NO")
    private BigDecimal     endorsementNo ;

    @Column(name="PRODUCT_ID")
    private String     productId ;

    @Column(name="SUB_CLASS")
    private String     subClass ;
    

    
    @Column(name="COMMISSION_TYPE")
    private String     commissionType ;
    
    @Column(name="LOGIN_ID")
    private String     loginId ;
    
    @Column(name="PROFIT_COM_STATUS")
    private String     profitComStatus ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENTRY_DATE")
    private Date    entryDate ;

}
