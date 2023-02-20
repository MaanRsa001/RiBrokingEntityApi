package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@IdClass(MailNotificationDetailId.class)
@Table(name="MAIL_NOTIFICATION_DETAIL")
public class MailNotificationDetail implements Serializable { 
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="MAIL_RECORD_NO", nullable=false)
	    private BigDecimal mailRecordNo ;

	    @Id
	    @Column(name="MAIL_TYPE", nullable=false)
	    private String     mailType ;

	    @Id
	    @Column(name="PROPOSAL_NO", nullable=false)
	    private BigDecimal proposalNo ;

	    @Id
	    @Column(name="SNO", nullable=false)
	    private BigDecimal sno ;

	    @Id
	    @Column(name="BRANCH_CODE", nullable=false)
	    private String     branchCode ;

	    //--- ENTITY DATA FIELDS 
	    @Column(name="BASE_PROPOSAL_NO")
	    private BigDecimal baseProposalNo ;

	    @Column(name="BOUQUET_NO")
	    private BigDecimal     bouquetNo ;

	    @Column(name="BROKER_ID")
	    private String     brokerId ;

	    @Column(name="CEDING_COMPANY_ID")
	    private String     cedingCompanyId ;
	    
	    @Column(name="CLIENT_ID")
	    private String     clientId ;
	    
	    @Column(name="CONTRACT_NO")
	    private BigDecimal     contractNo ;
	    
	    @Column(name="LAYER_NO")
	    private BigDecimal     layerNo ;
	    
	    @Column(name="MAIL_BODY")
	    private String     mailBody ;
	    
	    @Column(name="MAIL_CC")
	    private String     mailCc ;
	    
	    @Column(name="MAIL_STATUS")
	    private String     mailStatus ;
	    
	    @Column(name="MAIL_SUBJECT")
	    private String     mailSubject ;
	    
	    @Column(name="MAIL_TO")
	    private String     mailTo ;
	    
	    @Column(name="REINSURER_ID")
	    private String reinsurerId ;

	    @Column(name="REMARKS")
	    private String     remarks ;

	    @Column(name="SECTION_NO")
	    private BigDecimal     sectionNo ;

	    @Column(name="STATUS_NO")
	    private String     statusNo ;

	    @Column(name="USER_ID")
	    private String userId ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="UPDATE_DATE")
	    private Date     updateDate ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="ENTRY_DATE")
	    private Date       entryDate ;
	    
	    //--- ENTITY LINKS ( RELATIONSHIP )
	   
	  //  
	    
//	    @ManyToOne //(fetch = FetchType.LAZY)
//	    @JoinColumns({
//	    	@JoinColumn(name="REINSURER_ID", referencedColumnName = "REINSURER_ID"),  //, insertable =false, updatable = false
//	    	@JoinColumn(name="BROKER_ID", referencedColumnName = "BROKER_ID"),
//	    	@JoinColumn(name="PROPOSAL_NO", referencedColumnName = "PROPOSAL_NO")})
//	    private TtrnRiPlacement ttrnRiPlacement;
	}




