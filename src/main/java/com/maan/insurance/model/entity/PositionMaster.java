/* 
*  Copyright (c) 2019. All right reserved
 * Created on 2022-09-15 ( Date ISO 2022-09-15 - Time 15:42:01 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */

/*
 * Created on 2022-09-15 ( 15:42:01 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */


package com.maan.insurance.model.entity;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;




/**
* Domain class for entity "PositionMaster"
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
@IdClass(PositionMasterId.class)
@Table(name="POSITION_MASTER")


public class PositionMaster implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="PROPOSAL_NO", nullable=false)
    private BigDecimal proposalNo ;

    @Id
    @Column(name="AMEND_ID", nullable=false)
    private BigDecimal amendId ;


    @Column(name="CONTRACT_STATUS")
    private String     contractStatus ;

   
    @Column(name="LOGIN_ID", nullable=false, length=100)
    private String     loginId ;

    //--- ENTITY DATA FIELDS 
    @Column(name="CONTRACT_NO")
    private BigDecimal contractNo ;

    @Column(name="LAYER_NO")
    private BigDecimal layerNo ;

    @Column(name="REINSURANCE_ID", length=20)
    private String     reinsuranceId ;

    @Column(name="PRODUCT_ID")
    private BigDecimal productId ;

    @Column(name="DEPT_ID", length=20)
    private String     deptId ;

    @Column(name="CEDING_COMPANY_ID", length=20)
    private String     cedingCompanyId ;

    @Column(name="BROKER_ID", length=20)
    private String     brokerId ;

    @Column(name="UW_YEAR", length=20)
    private String     uwYear ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UW_MONTH")
    private Date       uwMonth ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ACCOUNT_DATE")
    private Date       accountDate ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="INCEPTION_DATE")
    private Date       inceptionDate ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EXPIRY_DATE")
    private Date       expiryDate ;

    @Column(name="PROPOSAL_STATUS", length=5)
    private String     proposalStatus ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENTRY_DATE")
    private Date       entryDate ;

    @Column(name="BASE_LAYER", length=20)
    private String     baseLayer ;

    @Column(name="BRANCH_CODE", length=20)
    private String     branchCode ;

    @Column(name="OLD_CONTRACTNO", length=30)
    private String     oldContractno ;

    @Column(name="RENEWAL_STATUS", length=10)
    private String     renewalStatus ;

    @Column(name="RETRO_TYPE", length=10)
    private String     retroType ;

    @Column(name="FAC_CONTRACT_NO", length=30)
    private String     facContractNo ;

    @Column(name="UPDATE_LOGIN_ID", length=100)
    private String     updateLoginId ;

    @Column(name="ENDT_STATUS", length=1)
    private String     endtStatus ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENDORSEMENT_DATE")
    private Date       endorsementDate ;

    @Column(name="CEASE_STATUS", length=10)
    private String     ceaseStatus ;

    @Column(name="RSK_DUMMY_CONTRACT", length=2)
    private String     rskDummyContract ;

    @Column(name="EDIT_MODE", length=20)
    private String     editMode ;

    @Column(name="DATA_MAP_CONT_NO", length=30)
    private String     dataMapContNo ;
    
    @Column(name="BOUQUET_NO")
    private BigDecimal     bouquetNo ; //RIBROKING
    
    @Column(name="BOUQUET_MODE_YN")
    private String     bouquetModeYn ;
    
    @Column(name="UW_YEAR_TO")
    private BigDecimal     uwYearTo ;
    
    @Column(name="SECTION_NO")
    private BigDecimal     sectionNo ;
    
    @Column(name="OFFER_NO")
    private String     offerNo ;
    
    @Column(name="NEW_LAYER_NO")
    private String     newLayerNo ;

    //--- ENTITY LINKS ( RELATIONSHIP )
  

}



