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

@Table(name="session_master") 
public class SessionMaster implements Serializable {
	
	private static final long serialVersionUID = 1L;
	 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="SESSION_ID", nullable=false)
    private String sessionId ;
    
  //--- ENTITY DATA FIELDS 
    @Column(name="LOGIN_ID")
    private String     loginId ;

    @Column(name="IP_ADDRESS")
    private String     ipAddress ;
  
    @Column(name="LOGIN_DT")
    private String       loginDt ;

    @Column(name="BRANCH_CODE")
    private String     branchCode ;

    @Column(name="LOGOUT_DT")
    private String     logoutDt ;

    @Column(name="REMARKS")
    private String       remarks ;

    @Column(name="STATUS")
    private String     status ;

    @Column(name="INVALD_LOGIN_ID")
    private String     invaldLoginId ;
}
