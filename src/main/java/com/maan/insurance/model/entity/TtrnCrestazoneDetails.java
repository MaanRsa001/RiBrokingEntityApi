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
@Table(name="TTRN_CRESTAZONE_DETAILS")
public class TtrnCrestazoneDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="PROPOSAL_NO", nullable=false)
    private BigDecimal proposalNo ;
    
    //--- ENTITY DATA FIELDS 
    @Column(name="CONTRACT_NO")
    private BigDecimal contractNo ;
    
    @Column(name="AMEND_ID")
    private String     amendId ;
    
    @Column(name="SUB_CLASS")
    private String     subClass ;
    

    @Column(name="CRESTA_ID")
    private String crestaId ;

    @Column(name="STATUS")
    private String     status ;


    @Column(name="CRESTA_NAME")
    private String     crestaName ;

    @Column(name="CURRENCY")
    private String     currency ;

    @Column(name="ACC_RISK")
    private String     accRisk ;
    
    @Column(name="BRANCH_CODE")
    private String     branchCode ;
    
    @Column(name="TERITORY_CODE")
    private String     territoryCode ;
    
    @Column(name="SNO")
    private String     sno ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Entry_DATE")
    private Date    entryDate ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ACCUM_DATE")
    private Date    accumDate ;
}
