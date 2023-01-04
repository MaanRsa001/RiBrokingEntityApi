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
@Table(name="TTRN_RSK_CLASS_LIMITS")
public class TtrnRskClassLimits implements Serializable{ 
	private static final long serialVersionUID = 1L; 
	 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="RSK_PROPOSAL_NUMBER", nullable=false)
    private BigDecimal rskProposalNumber ;
    
    //--- ENTITY DATA FIELDS 
    @Column(name="RSK_ENDORSEMENT_NO")
    private BigDecimal rskEndorsementNo ;
    
    @Column(name="RSK_CONTRACT_NO")
    private BigDecimal rskContractNo ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENTRY_DATE")
    private Date     entryDate ;
    
    @Column(name="RSK_LAYER_NO")
    private BigDecimal     rskLayerNo ;
    

    @Column(name="RSK_PRODUCTID")
    private BigDecimal rskProductid ;

    @Column(name="RSK_COVER_CLASS")
    private String     rskCoverClass ;


    @Column(name="RSK_COVER_LIMT")
    private BigDecimal     rskCoverLimt ;

    @Column(name="RSK_COVER_LIMT_PERCENTAGE")
    private BigDecimal     rskCoverLimtPercentage ;
    
    @Column(name="RSK_DEDUCTABLE_LIMT")
    private BigDecimal     rskDeductableLimt ;

    @Column(name="RSK_DEDUCTABLE_PERCENTAGE")
    private BigDecimal     rskDeductablePercentage ;

    @Column(name="BRANCH_CODE")
    private String     branchCode ;
    
    @Column(name="STATUS")
    private String     status;

    
    @Column(name="RSK_SNO")
    private BigDecimal     rskSno ;
    
    @Column(name="RSK_EGNPI_AS_OFF")
    private BigDecimal     rskEgnpiAsOff ;
    
    @Column(name="RSK_COVER_SUB_CLASS")
    private String     rskCoverSubClass;
    
    @Column(name="RSK_GNPI_AS_OFF")
    private String     rskGnpiAsOff;
}

