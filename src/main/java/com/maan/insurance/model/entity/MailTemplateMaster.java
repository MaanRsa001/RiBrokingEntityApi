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
@IdClass(MailTemplateMasterId.class)
@Table(name="MAIL_TEMPLATE_MASTER")
public class MailTemplateMaster implements Serializable { 
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="MAIL_TYPE", nullable=false)
	    private String mailType ;

	    @Id
	    @Column(name="SNO", nullable=false)
	    private String sno ;

	    @Id
	    @Column(name="BRANCH_CODE", nullable=false, length=8)
	    private String     branchCode ;

	    //--- ENTITY DATA FIELDS 
	    @Column(name="EMAIL_CC")
	    private String emailCc ;

	    @Column(name="EMAIL_TO")
	    private String     emailTo ; 

	    @Column(name="MAIL_SUBJECT")
	    private String     mailSubject ;

	    @Column(name="MAIL_BODY")
	    private String     mailBody ;

	    @Column(name="MAIL_REGARDS")
	    private String     mailRegards ;

	


	    //--- ENTITY LINKS ( RELATIONSHIP )


	}