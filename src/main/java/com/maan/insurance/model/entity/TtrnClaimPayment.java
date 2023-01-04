/* 
*  Copyright (c) 2019. All right reserved
 * Created on 2022-09-15 ( Date ISO 2022-09-15 - Time 15:43:16 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */

/*
 * Created on 2022-09-15 ( 15:43:16 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */


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




/**
* Domain class for entity "TtrnClaimPayment"
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
@IdClass(TtrnClaimPaymentId.class)
@Table(name="TTRN_CLAIM_PAYMENT")


public class TtrnClaimPayment implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="SL_NO", nullable=false)
    private BigDecimal slNo ;

    @Id
    @Column(name="CLAIM_NO", nullable=false)
    private BigDecimal claimNo ;

    @Id
    @Column(name="CONTRACT_NO", nullable=false, length=30)
    private String     contractNo ;

    //--- ENTITY DATA FIELDS 
    @Column(name="LAYER_NO")
    private BigDecimal layerNo ;

    @Column(name="PAYMENT_REQUEST_NO", length=100)
    private String     paymentRequestNo ;

    @Column(name="PAID_AMOUNT_OC")
    private BigDecimal paidAmountOc ;

    @Column(name="PAID_AMOUNT_DC")
    private BigDecimal paidAmountDc ;

    @Column(name="LOSS_ESTIMATE_REVISED_OC")
    private BigDecimal lossEstimateRevisedOc ;

    @Column(name="LOSS_ESTIMATE_REVISED_DC")
    private BigDecimal lossEstimateRevisedDc ;

    @Column(name="CLAIM_NOTE_RECOMM", length=100)
    private String     claimNoteRecomm ;

    @Column(name="PAYMENT_REFERENCE", length=100)
    private String     paymentReference ;

    @Column(name="ADVICE_TREASURY", length=10)
    private String     adviceTreasury ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="INCEPTION_DATE")
    private Date       inceptionDate ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EXPIRY_DATE")
    private Date       expiryDate ;

    @Column(name="REMARKS", length=100)
    private String     remarks ;

    @Column(name="STATUS", length=1)
    private String     status ;

    @Column(name="CLAIM_PAYMENT_NO")
    private BigDecimal claimPaymentNo ;

    @Column(name="ADVICE_TREASURY_EMAILID", length=200)
    private String     adviceTreasuryEmailid ;

    @Column(name="ALLOCATED_TILL_DATE")
    private BigDecimal allocatedTillDate ;

    @Column(name="ACC_CLAIM", length=30)
    private String     accClaim ;

    @Column(name="CHECKYN", length=1)
    private String     checkyn ;

    @Column(name="RESERVE_ID")
    private BigDecimal reserveId ;

    @Column(name="SETTLEMENT_STATUS", length=20)
    private String     settlementStatus ;

    @Column(name="REINSTATEMENT_TYPE", length=50)
    private String     reinstatementType ;

    @Column(name="REINSPREMIUM_OURSHARE_OC")
    private BigDecimal reinspremiumOurshareOc ;

    @Column(name="REINSPREMIUM_OURSHARE_DC")
    private BigDecimal reinspremiumOurshareDc ;

    @Column(name="NETCLAIMAMT_OURSHARE_OC")
    private BigDecimal netclaimamtOurshareOc ;

    @Column(name="NETCLAIMAMT_OURSHARE_DC")
    private BigDecimal netclaimamtOurshareDc ;

    @Column(name="EXCHANGE_RATE")
    private BigDecimal exchangeRate ;

    @Column(name="INSURED_NAME", length=200)
    private String     insuredName ;

    @Column(name="CASH_LOSS_SETTLED_TILLDATE")
    private BigDecimal cashLossSettledTilldate ;

    @Column(name="BRANCH_CODE", length=20)
    private String     branchCode ;

    @Column(name="LOGIN_ID", length=100)
    private String     loginId ;

    @Column(name="PAID_CLAIM_OS_OC")
    private BigDecimal paidClaimOsOc ;

    @Column(name="PAID_CLAIM_OS_DC")
    private BigDecimal paidClaimOsDc ;

    @Column(name="SAF_OS_OC")
    private BigDecimal safOsOc ;

    @Column(name="SAF_OS_DC")
    private BigDecimal safOsDc ;

    @Column(name="OTH_FEE_OS_OC")
    private BigDecimal othFeeOsOc ;

    @Column(name="OTH_FEE_OS_DC")
    private BigDecimal othFeeOsDc ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="SYS_DATE")
    private Date       sysDate ;

    @Column(name="PAYMENT_TYPE", length=20)
    private String     paymentType ;


    //--- ENTITY LINKS ( RELATIONSHIP )


}



