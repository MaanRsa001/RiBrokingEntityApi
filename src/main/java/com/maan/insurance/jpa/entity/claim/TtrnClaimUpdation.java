package com.maan.insurance.jpa.entity.claim;

/* 
 * Created on 2022-09-15 ( Date ISO 2022-09-15 - Time 15:43:20 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */

/*
 * Created on 2022-09-15 ( 15:43:20 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */




import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.maan.insurance.jpa.keys.TtrnClaimUpdationKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




/**
* Domain class for entity "TtrnClaimUpdation"
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
@IdClass(TtrnClaimUpdationKey.class)
@Table(name="TTRN_CLAIM_UPDATION")

public class TtrnClaimUpdation implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="SL_NO", nullable=false)
    private Integer slNo ;

    @Id
    @Column(name="CLAIM_NO", nullable=false, length=30)
    private String     claimNo ;

    @Id
    @Column(name="CONTRACT_NO", nullable=false, length=30)
    private String     contractNo ;

    //--- ENTITY DATA FIELDS 
    @Column(name="LAYER_NO")
    private BigDecimal layerNo ;

    @Column(name="LOSS_ESTIMATE_REVISED_OC")
    private BigDecimal lossEstimateRevisedOc ;

    @Column(name="LOSS_ESTIMATE_REVISED_DC")
    private BigDecimal lossEstimateRevisedDc ;

    @Column(name="UPDATE_REFERENCE", length=100)
    private String     updateReference ;

    @Column(name="INCEPTION_DATE")
    private Date       inceptionDate ;

    @Column(name="EXPIRY_DATE")
    private Date       expiryDate ;

    @Column(name="REMARKS", length=500)
    private String     remarks ;

    @Column(name="STATUS")
    private String     status ;

    @Column(name="BRANCH_CODE", length=20)
    private String     branchCode ;

    @Column(name="LOGIN_ID", length=100)
    private String     loginId ;

    @Column(name="SAF_100_OC", length=100)
    private String     saf100Oc ;

    @Column(name="SAF_100_DC", length=100)
    private String     saf100Dc ;

    @Column(name="OTH_FEE_100_OC", length=100)
    private String     othFee100Oc ;

    @Column(name="OTH_FEE_100_DC", length=100)
    private String     othFee100Dc ;

    @Column(name="C_IBNR_100_OC", length=100)
    private String     cIbnr100Oc ;

    @Column(name="C_IBNR_100_DC", length=100)
    private String     cIbnr100Dc ;

    @Column(name="SAF_OS_DC", length=100)
    private String     safOsDc ;

    @Column(name="OTH_FEE_OS_DC", length=100)
    private String     othFeeOsDc ;

    @Column(name="C_IBNR_OS_DC", length=100)
    private String     cIbnrOsDc ;

    @Column(name="LOSS_ESTIMATE_100_OC", length=100)
    private String     lossEstimate100Oc ;

    @Column(name="LOSS_ESTIMATE_100_DC", length=100)
    private String     lossEstimate100Dc ;

    @Column(name="RESERVE_FEES", length=100)
    private String     reserveFees ;

    @Column(name="RESERVE_IBNR", length=100)
    private String     reserveIbnr ;

    @Column(name="SAF_OS_OC")
    private BigDecimal safOsOc ;

    @Column(name="OTH_FEE_OS_OC")
    private BigDecimal othFeeOsOc ;

    @Column(name="C_IBNR_OS_OC")
    private BigDecimal cIbnrOsOc ;

    @Column(name="TOT_RES_AMOUNT_OC")
    private BigDecimal totResAmountOc ;

    @Column(name="TOT_RES_AMOUNT_DC")
    private BigDecimal totResAmountDc ;

    @Column(name="EXCHANGE_RATE")
    private BigDecimal exchangeRate ;

    @Column(name="RES_POS_DATE")
    private Date       resPosDate ;

    @Column(name="SYS_DATE")
    private Date       sysDate ;


    //--- ENTITY LINKS ( RELATIONSHIP )


}



