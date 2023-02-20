package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
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
@IdClass(TtrnRiPlacementId.class)
@Table(name="TTRN_RI_PLACEMENT")
public class TtrnRiPlacement implements Serializable { 
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="PLACEMENT_NO", nullable=false)
	    private BigDecimal     placementNo ;

	    @Id
	    @Column(name="STATUS_NO", nullable=false)
	    private BigDecimal statusNo ;
	    
	    @Id
	    @Column(name="SNO", nullable=false)
	    private BigDecimal sno ;
	    
	    @Id
	    @Column(name="PROPOSAL_NO", nullable=false)
	    private BigDecimal proposalNo ;
	    
	    @Id
	    @Column(name="BRANCH_CODE")
	    private String branchCode ;

	    //--- ENTITY DATA FIELDS 
	    @Column(name="BOUQUET_NO")
	    private BigDecimal     bouquetNo ;

	    @Column(name="BASE_PROPOSAL_NO")
	    private BigDecimal baseProposalNo ;

	    @Column(name="CONTRACT_NO")
	    private BigDecimal contractNo ;

	    @Column(name="LAYER_NO")
	    private BigDecimal layerNo ;

	    @Column(name="SECTION_NO")
	    private BigDecimal     sectionNo ;

	    @Column(name="AMEND_ID")
	    private BigDecimal     amendId ;

	    @Column(name="CEDING_COMPANY_ID")
	    private String cedingCompanyId ;

	    @Column(name="REINSURER_ID")
	    private String reinsurerId ;

	    @Column(name="BROKER_ID")
	    private String brokerId ;

	    @Column(name="SHARE_OFFERED")
	    private BigDecimal     shareOffered ;
	    
	    @Column(name="SHARE_WRITTEN")
	    private BigDecimal     shareWritten ;


	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="WRITTEN_LINE_VALIDITY")
	    private Date       writtenLineValidity ;

	    @Column(name="SHARE_PROPOSAL_WRITTEN")
	    private BigDecimal shareProposalWritten ;

	    @Column(name="WRITTEN_LINE_REMARKS")
	    private String     writtenLineRemarks ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="SHARE_LINE_VALIDITY")
	    private Date       shareLineValidity ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="SYS_DATE")
	    private Date       sysDate ;

	  
	    @Column(name="SHARE_SIGNED")
	    private BigDecimal    shareSigned ;

	    @Column(name="SHARE_LINE_REMARKS")
	    private String shareLineRemarks ;

	    @Column(name="SHARE_PROPOSED_SIGNED")
	    private BigDecimal     shareProposedSigned ;

	    @Column(name="BROKERAGE")
	    private BigDecimal     brokerage ;

	    @Column(name="EMAIL_RECORDID")
	    private String     emailRecordid ;

	    @Column(name="REINSURER_CONTRACT_NO")
	    private String     reinsurerContractNo ;

	    @Column(name="PLACEMENT_MODE")
	    private String     placementMode ;

	    @Column(name="PLACEMENT_AMEND_ID")
	    private BigDecimal    placementAmendId ;

	    @Column(name="STATUS")
	    private String     status ;

	    @Column(name="APPROVE_STATUS")
	    private String     approveStatus ;

	    @Column(name="USER_ID")
	    private String userId ;

	    @Column(name="REMARKS")
	    private String remarks ;
	
	    
	    //--- ENTITY LINKS (RELATIONSHIP)
//	    @OneToMany(mappedBy = "ttrnRiPlacement")
//	    private List<MailNotificationDetail> listOfMailNotificationDetail;
	    

	}



