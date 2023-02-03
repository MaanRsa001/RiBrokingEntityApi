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
@IdClass(TtrnRiPlacementStatusId.class)
@Table(name="TTRN_RI_PLACEMENT_STATUS")
public class TtrnRiPlacementStatus implements Serializable { 
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="BROKER_ID", nullable=false)
	    private String     brokerId ;
	    
	    @Id
	    @Column(name="REINSURER_ID")
	    private String reinsurerId ;

	    @Id
	    @Column(name="STATUS_NO", nullable=false)
	    private BigDecimal statusNo ;
	    
	    @Id
	    @Column(name="SNO")
	    private BigDecimal sno ;
	    
	    @Id
	    @Column(name="CORRESPONDENT_ID")
	    private BigDecimal     correspondentId ;
	    
	    @Id
	    @Column(name="PROPOSAL_NO")
	    private BigDecimal proposalNo ;
	    
	    @Id
	    @Column(name="BRANCH_CODE")
	    private String branchCode ;

	    //--- ENTITY DATA FIELDS 
	    @Column(name="BOUQUET_NO")
	    private BigDecimal     bouquetNo ;

	    @Column(name="BASE_PROPOSAL_NO")
	    private BigDecimal baseProposalNo ;

	    @Column(name="AMEND_ID")
	    private BigDecimal amendId ;

	    @Column(name="APPROVER_STATUS")
	    private String approverStatus ;

	    @Column(name="CEDENT_CORRESPONDENCE")
	    private String     cedentCorrespondence ;

	    @Column(name="CURRENT_STATUS")
	    private String currentStatus ;


	    @Column(name="EMAIL_BY")
	    private String emailBy ;

	    @Column(name="EMAIL_RECORDID")
	    private String     emailRecordid ;
	    
	    @Column(name="TQR_CORRESPONDENCE")
	    private String     tqrCorrespondence ;


	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="SYS_DATE")
	    private Date       sysDate ;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="UPDATE_DATE")
	    private Date       updateDate ;


	  
	    @Column(name="USER_ID")
	    private String     userId ;

	  

	    @Column(name="NEW_STATUS")
	    private String     newStatus ;

	    @Column(name="REINSURER_CORRESPONDENCE")
	    private String     reinsurerCorrespondence ;



	    @Column(name="STATUS")
	    private String     status ;



	    @Column(name="REMARKS")
	    private String remarks ;

	 
	    //--- ENTITY LINKS ( RELATIONSHIP )


	}




