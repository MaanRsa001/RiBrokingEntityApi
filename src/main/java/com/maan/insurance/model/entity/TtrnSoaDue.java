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
@IdClass(TtrnSoaDueId.class)
@Table(name="TTRN_SOA_DUE")
public class TtrnSoaDue  implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="BRANCH_CODE", nullable=false)
	    private String     branchCode ;

	    @Id
	    @Column(name="CONTRACT_NO", nullable=false)
	    private BigDecimal contractNo ;
	    
	    @Id
	    @Column(name="PROPOSAL_NO", nullable=false)
	    private BigDecimal proposalNo ;
	    
	    //--- ENTITY DATA FIELDS 
	  
	  
	    @Column(name="DEPT_ID")
	    private String     deptId ;
	    @Column(name="AMEND_ID")
	    private BigDecimal     amendId ;
	    @Column(name="TREATY_TYPE")
	    private String     treatyType ;
	    @Column(name="PERIODICITY")
	    private String     periodicity ;
	   
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="INCEPTION_DATE")
	    private Date       inceptionDate ;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="EXPIRY_DATE")
	    private Date       expiryDate ;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="STATEMENT_DATE")
	    private Date       statementDate ;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="SYS_DATE")
	    private Date       sysDate ;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="STATEMENT_DUE_DATE")
	    private Date       statementDueDate ;

	

	    //--- ENTITY LINKS ( RELATIONSHIP )


	}


