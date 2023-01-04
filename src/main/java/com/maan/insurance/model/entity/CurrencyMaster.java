/* 
*  Copyright (c) 2019. All right reserved
 * Created on 2022-09-15 ( Date ISO 2022-09-15 - Time 15:41:34 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */

/*
 * Created on 2022-09-15 ( 15:41:34 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */


package com.maan.insurance.model.entity;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Table;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;




/**
* Domain class for entity "CurrencyMaster"
*
* @author Telosys Tools Generator
*
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@IdClass(CurrencyMasterId.class)
@Table(name="CURRENCY_MASTER")


public class CurrencyMaster implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="CURRENCY_ID", nullable=false)
    private BigDecimal currencyId ;

    @Id
    @Column(name="AMEND_ID", nullable=false)
    private BigDecimal amendId ;

    @Id
    @Column(name="COUNTRY_ID", nullable=false)
    private BigDecimal countryId ;

    @Id
    @Column(name="BRANCH_CODE", nullable=false, length=8)
    private String     branchCode ;

    //--- ENTITY DATA FIELDS 
    @Column(name="CURRENCY_NAME", length=25)
    private String     currencyName ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="INCEPTION_DATE")
    private Date       inceptionDate ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EXPIRY_DATE")
    private Date       expiryDate ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EFFECTIVE_DATE")
    private Date       effectiveDate ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENTRY_DATE")
    private Date       entryDate ;

    @Column(name="REMARKS", length=100)
    private String     remarks ;

    @Column(name="STATUS", length=1)
    private String     status ;

    @Column(name="CORE_APP_CODE", length=10)
    private String     coreAppCode ;

    @Column(name="SHORT_NAME", length=25)
    private String     shortName ;

    @Column(name="SNO")
    private BigDecimal sno ;

    @Column(name="RSA_FACTOR")
    private BigDecimal rsaFactor ;


    //--- ENTITY LINKS ( RELATIONSHIP )


}



