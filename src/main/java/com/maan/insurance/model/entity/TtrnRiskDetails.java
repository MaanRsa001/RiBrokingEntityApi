/* 
*  Copyright (c) 2019. All right reserved
 * Created on 2022-09-15 ( Date ISO 2022-09-15 - Time 15:44:14 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */

/*
 * Created on 2022-09-15 ( 15:44:14 )
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
* Domain class for entity "TtrnRiskDetails"
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
@IdClass(TtrnRiskDetailsId.class)
@Table(name="TTRN_RISK_DETAILS")


public class TtrnRiskDetails implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="RSK_PROPOSAL_NUMBER", nullable=false, length=35)
    private String     rskProposalNumber ;

    @Id
    @Column(name="RSK_ENDORSEMENT_NO", nullable=false)
    private BigDecimal rskEndorsementNo ;

    //--- ENTITY DATA FIELDS 
    @Column(name="RSK_CONTRACT_NO", length=30)
    private String     rskContractNo ;

    @Column(name="RSK_LAYER_NO")
    private BigDecimal rskLayerNo ;

    @Column(name="RSK_PRODUCTID")
    private BigDecimal rskProductid ;

    @Column(name="RSK_DEPTID")
    private BigDecimal rskDeptid ;

    @Column(name="RSK_PFCID", length=10)
    private String     rskPfcid ;

    @Column(name="RSK_SPFCID", length=1000)
    private String     rskSpfcid ;

    @Column(name="RSK_POLBRANCH")
    private BigDecimal rskPolbranch ;

    @Column(name="RSK_CEDINGID")
    private BigDecimal rskCedingid ;

    @Column(name="RSK_BROKERID")
    private BigDecimal rskBrokerid ;

    @Column(name="RSK_TREATYID", length=500)
    private String     rskTreatyid ;

    @Column(name="RSK_MONTH")
    private Date       rskMonth ;

    @Column(name="RSK_UWYEAR")
    private BigDecimal rskUwyear ;

    @Column(name="RSK_UNDERWRITTER", length=500)
    private String     rskUnderwritter ;

    @Column(name="RSK_INCEPTION_DATE")
    private Date       rskInceptionDate ;

    @Column(name="RSK_EXPIRY_DATE")
    private Date       rskExpiryDate ;

    @Column(name="RSK_ACCOUNT_DATE")
    private Date       rskAccountDate ;

    @Column(name="RSK_ORIGINAL_CURR", length=500)
    private String     rskOriginalCurr ;

    @Column(name="RSK_EXCHANGE_RATE")
    private BigDecimal rskExchangeRate ;

    @Column(name="RSK_BASIS", length=10)
    private String     rskBasis ;

    @Column(name="RSK_PERIOD_OF_NOTICE", length=50)
    private String     rskPeriodOfNotice ;

    @Column(name="RSK_RISK_COVERED", length=100)
    private String     rskRiskCovered ;

    @Column(name="RSK_TERRITORY_SCOPE", length=500)
    private String     rskTerritoryScope ;

    @Column(name="RSK_TERRITORY", length=500)
    private String     rskTerritory ;

    @Column(name="RSK_ENTRY_DATE")
    private Date       rskEntryDate ;

    @Column(name="RSK_END_DATE")
    private Date       rskEndDate ;

    @Column(name="RSK_STATUS", length=10)
    private String     rskStatus ;

    @Column(name="RSK_REMARKS", length=500)
    private String     rskRemarks ;

    @Column(name="RSK_PROPOSAL_TYPE", length=10)
    private String     rskProposalType ;

    @Column(name="RSK_ACCOUNTING_PERIOD")
    private BigDecimal rskAccountingPeriod ;

    @Column(name="RSK_RECEIPT_STATEMENT")
    private BigDecimal rskReceiptStatement ;

    @Column(name="RSK_RECEIPT_PAYEMENT")
    private BigDecimal rskReceiptPayement ;

    @Column(name="RSK_INSURED_NAME", length=100)
    private String     rskInsuredName ;

    @Column(name="RSK_LOCATION", length=30)
    private String     rskLocation ;

    @Column(name="RSK_CITY", length=15)
    private String     rskCity ;

    @Column(name="RSK_BASE_LAYER")
    private BigDecimal rskBaseLayer ;

    @Column(name="MND_INSTALLMENTS")
    private BigDecimal mndInstallments ;

    @Column(name="OLD_CONTRACTNO", length=30)
    private String     oldContractno ;

    @Column(name="RSK_PREMIUM_RATE")
    private BigDecimal rskPremiumRate ;

    @Column(name="RETRO_CESSIONARIES")
    private BigDecimal retroCessionaries ;

    @Column(name="RSK_RETRO_TYPE", length=10)
    private String     rskRetroType ;

    @Column(name="INWARD_BUS_TYPE", length=10)
    private String     inwardBusType ;

    @Column(name="TREATYTYPE", length=10)
    private String     treatytype ;

    @Column(name="RSK_BUSINESS_TYPE", length=10)
    private String     rskBusinessType ;

    @Column(name="RSK_EXCHANGE_TYPE", length=10)
    private String     rskExchangeType ;

    @Column(name="RSK_PERILS_COVERED", length=100)
    private String     rskPerilsCovered ;

    @Column(name="RSK_LOC_ISSUED", length=10)
    private String     rskLocIssued ;

    @Column(name="RSK_UMBRELLA_XL", length=10)
    private String     rskUmbrellaXl ;

    @Column(name="LOGIN_ID", length=100)
    private String     loginId ;

    @Column(name="BRANCH_CODE", length=20)
    private String     branchCode ;

    @Column(name="SYS_DATE")
    private Date       sysDate ;

    @Column(name="COUNTRIES_INCLUDE", length=2000)
    private String     countriesInclude ;

    @Column(name="COUNTRIES_EXCLUDE", length=2000)
    private String     countriesExclude ;

    @Column(name="RSK_NO_OF_LINE", length=100)
    private String     rskNoOfLine ;

    @Column(name="RSK_LATITUDE")
    private BigDecimal rskLatitude ;

    @Column(name="RSK_LONGITUDE")
    private BigDecimal rskLongitude ;

    @Column(name="RSK_VESSAL_TONNAGE")
    private BigDecimal rskVessalTonnage ;

    @Column(name="RS_ENDORSEMENT_TYPE", length=20)
    private String     rsEndorsementType ;

    @Column(name="RSK_DOC_STATUS", length=20)
    private String     rskDocStatus ;

    @Column(name="RSK_RUN_OFF_YEAR")
    private BigDecimal rskRunOffYear ;

    @Column(name="RSK_LOC_BNK_NAME", length=100)
    private String     rskLocBnkName ;

    @Column(name="RSK_LOC_CRDT_PRD")
    private BigDecimal rskLocCrdtPrd ;

    @Column(name="RSK_LOC_CRDT_AMT")
    private BigDecimal rskLocCrdtAmt ;

    @Column(name="RSK_LOC_BENFCRE_NAME", length=100)
    private String     rskLocBenfcreName ;

    @Column(name="RSK_CESSION_EXG_RATE", length=10)
    private String     rskCessionExgRate ;

    @Column(name="RSK_FIXED_RATE")
    private BigDecimal rskFixedRate ;

    @Column(name="RSK_DUMMY_CONTRACT", length=2)
    private String     rskDummyContract ;

    @Column(name="RETENTIONYN", length=10)
    private String     retentionyn ;

    @Column(name="XOL_LAYER_NO", length=10)
    private String     xolLayerNo ;


    //--- ENTITY LINKS ( RELATIONSHIP )


}



