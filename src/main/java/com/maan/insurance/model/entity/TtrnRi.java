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
@IdClass(TtrnRiId.class)
@Table(name="TTRN_RI")
public class TtrnRi  implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="BRANCH_CODE", nullable=false)
	    private String     branchCode ;

	    @Id
	    @Column(name="RI_NO", nullable=false)
	    private BigDecimal riNo ;

	    @Id
	    @Column(name="STATUS_NO", nullable=false)
	    private BigDecimal statusNo ;
	    
	    @Id
	    @Column(name="SNO", nullable=false)
	    private BigDecimal sno ;
	    
	    @Id
	    @Column(name="PROPOSAL_NO", nullable=false)
	    private BigDecimal proposalNo ;
	    
	    //--- ENTITY DATA FIELDS 
	    @Column(name="BOUQUET_NO")
	    private BigDecimal bouquetNo ;

	    @Column(name="BASE_PROPOSAL_NO")
	    private BigDecimal     baseProposalNo ;
	    
	    @Column(name="CONTRACT_NO")
	    private BigDecimal contractNo ;

	    @Column(name="SUB_CONTRACT_NO")
	    private BigDecimal     subContractNo ;
	    
	    @Column(name="LAYER_NO")
	    private BigDecimal layerNo ;

	    @Column(name="SECTION_NO")
	    private BigDecimal     sectionNo ;
	    
	    @Column(name="AMEND_ID")
	    private BigDecimal amendId ;

	    @Column(name="REINSURER_ID")
	    private String     reinsurerId ;
	    
	    @Column(name="BROKER_ID")
	    private String brokerId ;

	    @Column(name="SHARE_OFFERED")
	    private BigDecimal     shareOffered ;
	    
	    @Column(name="SHARE_WRITTEN")
	    private BigDecimal shareWritten ;

	    @Column(name="SHARE_PROPOSAL_WRITTEN")
	    private BigDecimal     shareProposalWritten ;
	    
	    @Column(name="SHARE_SIGNED")
	    private BigDecimal shareSigned ;

	    @Column(name="SHARE_PROPOSED_SIGNED")
	    private BigDecimal     shareProposedSigned ;
	    
	    @Column(name="BROKERAGE")
	    private BigDecimal brokerage ;

	    @Column(name="EMAIL_RECORDID")
	    private String     emailRecordid ;
	    
	    @Column(name="REINSURER_CONTRACT_NO")
	    private String     reinsurerContractNo ;
	    @Column(name="CURRENT_STATUS")
	    private String     currentStatus ;
	    @Column(name="NEW_STATUS")
	    private String     newStatus ;
	    @Column(name="APPROVE_STATUS")
	    private String     approveStatus ;
	    @Column(name="USER_ID")
	    private String     userId ;
	    @Column(name="REMARKS")
	    private String     remarks ;
	  

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="SYS_DATE")
	    private Date       sysDate ;
	  

	

	    //--- ENTITY LINKS ( RELATIONSHIP )


	}


