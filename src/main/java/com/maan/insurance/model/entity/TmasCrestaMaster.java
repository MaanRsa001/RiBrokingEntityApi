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
@Table(name="TMAS_CRESTA_MASTER")
public class TmasCrestaMaster implements Serializable{ 
	private static final long serialVersionUID = 1L;
	 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="CRESTA_SNO", nullable=false)
    private BigDecimal crestaSno ;
    
    //--- ENTITY DATA FIELDS 
    @Column(name="BRANCH_CODE")
    private String branchCode ;
    
    @Column(name="CRESTA_ID")
    private String crestaId ;
   
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENTRY_DATE")
    private Date     entryDate ;
    
    @Column(name="CRESTA_NAME")
    private String     crestaName ;
    

    @Column(name="REMARKS")
    private String remarks ;

    @Column(name="STATUS")
    private String     status ;


    @Column(name="TERITORY_CODE")
    private String     territoryCode ;

    @Column(name="USER_ID")
    private String     userId ;

}
