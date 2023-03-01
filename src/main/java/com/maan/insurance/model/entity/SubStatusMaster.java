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
@IdClass(SubStatusMasterId.class)
@Table(name="SUB_STATUS_MASTER")
public class SubStatusMaster implements Serializable { 
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="SNO", nullable=false)
	    private BigDecimal sno ;

	    @Id
	    @Column(name="STATUS_CODE", nullable=false)
	    private String statusCode ;
	    
	    @Id
	    @Column(name="SUB_STATUS_CODE", nullable=false)
	    private String subStatusCode ;	
	    
	    @Id
	    @Column(name="BRANCH_CODE", nullable=false)
	    private String     branchCode ;

	    //--- ENTITY DATA FIELDS 
	   

	    @Column(name="SUB_STATUS_NAME")
	    private String     subStatusName ;

	    @Column(name="SUB_STATUS_DESCRIPTON")
	    private String     subStatusDescripton ;

	    @Column(name="STATUS")
	    private String     status ;

	    @Column(name="REMARKS")
	    private String remarks ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="ENTRY_DATE")
	    private Date       entryDate ;
	    
	    @Column(name="APPROVEL_YN")
	    private String approvelYN ;
	    
	    @Column(name="EMAIL_YN")
	    private String emailYN ;
	    //--- ENTITY LINKS ( RELATIONSHIP )
	    @Column(name="DISPLAY_ORDER", nullable=false)
	    private BigDecimal displayOrder ;

	}



