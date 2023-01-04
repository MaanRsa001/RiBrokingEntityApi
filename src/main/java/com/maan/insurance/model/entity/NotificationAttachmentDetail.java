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
@IdClass(NotificationAttachmentDetailId.class)
@Table(name="NOTIFICATION_ATTACHMENT_DETAIL")

public class NotificationAttachmentDetail implements Serializable { 
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="DOC_ID", nullable=false)
	    private BigDecimal docId ;

	    @Id
	    @Column(name="DOC_TYPE", nullable=false)
	    private String     docType ;

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

	    @Column(name="CORRESPONDENT_ID")
	    private BigDecimal     correspondentId ;
	    
	    
	    @Column(name="REINSURER_ID")
	    private String reinsurerId ;

	    @Column(name="REMARKS")
	    private String     remarks ;

	    @Column(name="STATUS_NO")
	    private BigDecimal     statusNo ;

	    @Column(name="USER_ID")
	    private String     userId ;

	    //--- ENTITY LINKS ( RELATIONSHIP )
	    @Column(name="DOC_DESCRIPTION")
	    private String docDescription ;

	    @Column(name="MAIL_RECORD_NO")
	    private BigDecimal     mailRecordNo ;

	    @Column(name="FILE_LOCATION")
	    private String     fileLocation ;

	    @Column(name="ORG_FILE_NAME")
	    private String     orgFileName ;
	    
	    @Column(name="OUR_FILE_NAME")
	    private String     ourFileName ;
	    

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="ENTRY_DATE")
	    private Date       entryDate ;


	}



