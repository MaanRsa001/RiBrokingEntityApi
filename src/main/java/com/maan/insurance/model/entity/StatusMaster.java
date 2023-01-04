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
@IdClass(StatusMasterId.class)
@Table(name="STATUS_MASTER")
public class StatusMaster implements Serializable { 
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="SNO", nullable=false)
	    private BigDecimal sno ;

	    @Id
	    @Column(name="STATUS_CODE", nullable=false)
	    private String statusCode ;
	    
	    @Id
	    @Column(name="BRANCH_CODE", nullable=false)
	    private String     branchCode ;

	    //--- ENTITY DATA FIELDS 
	   

	    @Column(name="STATUS_NAME")
	    private String     statusName ;

	    @Column(name="STATUS_DESCRIPTON")
	    private String     statusDescripton ;

	    @Column(name="STATUS")
	    private String     status ;

	    @Column(name="REMARKS")
	    private String remarks ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="ENTRY_DATE")
	    private Date       entryDate ;

	    //--- ENTITY LINKS ( RELATIONSHIP )


	}



