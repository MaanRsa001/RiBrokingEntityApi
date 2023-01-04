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
@Table(name="TTRN_DOC_UPLOAD_DETAILS")
public class TtrnDocUploadDetails implements Serializable {
	 
	private static final long serialVersionUID = 1L; 
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="DOC_ID", nullable=false, length=10)
	    private   BigDecimal   docId ;

	    //--- ENTITY DATA FIELDS 
	    @Column(name="DOC_TYPE", length=100)
	    private String     docType ;

	    @Column(name="DOC_DESC")
	    private String docDesc ;

	    @Column(name="ORG_FILE_NAME")
	    private String orgFileName ;

	    @Column(name="STATUS")
	    private String     status ;

	    @Column(name="OUR_FILE_NAME")
	    private String ourFileName ;

	    @Column(name="FILE_LOCATION")
	    private String     fileLocation ;

	    @Column(name="MODULE_TYPE")
	    private String     moduleType ;

	    @Column(name="PROPOSAL_NO")
	    private BigDecimal     proposalNo ;

	    @Column(name="CONTRACT_NO")
	    private BigDecimal    contractNo ;

	    @Column(name="LAYER_NO")
	    private BigDecimal     layerNo ;


	    @Column(name="TRAN_NO")
	    private BigDecimal     tranNo ;

	    @Column(name="PRODUCT_ID")
	    private BigDecimal    productId ;

	    @Column(name="BRANCH_CODE")
	    private String     branchCode ;
	    
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "EFF_DATE")
		private Date effDate;
		
	    @Column(name="LOGIN_ID")
	    private String     loginId ;

	    @Column(name="DEL_LOGIN_ID")
	    private String     delLoginId ;

	    @Column(name="ENDORSEMENT_NO")
	    private BigDecimal     endorsementNo ;

	    @Column(name="DEPTID")
	    private BigDecimal    deptId ;

	}




