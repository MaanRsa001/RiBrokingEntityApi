package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import java.sql.Timestamp;
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
@IdClass(TtrnPttySectionId.class)
@Table(name="TTRN_PTTY_SECTION")

public class TtrnPttySection implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	
	  //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="SECTION_NO", nullable=false)
    private String sectionNo ;

    @Id
    @Column(name="CONTRACT_NO", nullable=false)
    private String     contractNo ;
    
    @Id
    @Column(name="DEPT_ID", nullable=false)
    private String     deptId ;

    //--- ENTITY DATA FIELDS 
    @Column(name="SECTION_NAME")
    private String sectionName ;

    @Column(name="AMEND_ID")
    private String amendId ;

    @Column(name="BRANCH_CODE")
    private String branchCode ;

    @Column(name="LOGIN_ID")
    private String     loginId ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENTRY_DATE")
    private Date     entry_date ;



    //--- ENTITY LINKS ( RELATIONSHIP )

}
