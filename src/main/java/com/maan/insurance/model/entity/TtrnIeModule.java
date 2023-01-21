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
@Table(name="TTRN_IE_MODULE")
public class TtrnIeModule implements Serializable{ 
	private static final long serialVersionUID = 1L;
	 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="PROPOSAL_NO", nullable=false)
    private BigDecimal proposalNo ;
    
    //--- ENTITY DATA FIELDS 
    @Column(name="AMEND_ID")
    private BigDecimal amendId ;
    
    @Column(name="BRANCH_CODE")
    private String branchCode ;
   
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EEFECTIVE_DATE")
    private Date     eefectiveDate ;
    
    @Column(name="CONTRACT_NO")
    private String     contractNo ;
    

    @Column(name="ITEM_INCLUSION_EXCLUSION")
    private String itemInclusionExclusion ;

    @Column(name="ITEM_NO")
    private BigDecimal     itemNo ;


    @Column(name="ITEM_TYPE")
    private String     itemType ;

    @Column(name="LAYER_NO")
    private String     layerNo ;

    @Column(name="LOGIN_ID")
    private String     loginId ;
    
    @Column(name="TRANSACTION_NO")
    private BigDecimal     transactionNo;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="SYS_DATE")
    private Date     sysDate ;

}
