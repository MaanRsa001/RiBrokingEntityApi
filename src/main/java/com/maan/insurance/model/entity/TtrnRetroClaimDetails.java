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

import com.maan.insurance.model.entity.TtrnClaimDetails;
import com.maan.insurance.model.entity.TtrnClaimDetailsId;

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
@IdClass(TtrnRetroClaimDetailsId.class)
@Table(name="TTRN_RETRO_CLAIM_DETAILS")
public class TtrnRetroClaimDetails implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="CLAIM_NO", nullable=false)
	    private BigDecimal claimNo ;

	    @Id
	    @Column(name="CONTRACT_NO", nullable=false, length=30)
	    private String     contractNo ;

	    //--- ENTITY DATA FIELDS 
	    @Column(name="LAYER_NO")
	    private BigDecimal layerNo ;

	    @Column(name="STATUS_OF_CLAIM", length=10)
	    private String     statusOfClaim ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="DATE_OF_LOSS")
	    private Date       dateOfLoss ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="REPORT_DATE")
	    private Date       reportDate ;

	    @Column(name="LOSS_DETAILS", length=500)
	    private String     lossDetails ;

	    @Column(name="CAUSE_OF_LOSS", length=500)
	    private String     causeOfLoss ;

	    @Column(name="LOCATION", length=30)
	    private String     location ;

	    @Column(name="LOSS_ESTIMATE_OC")
	    private BigDecimal lossEstimateOc ;

	    @Column(name="LOSS_ESTIMATE_OS_OC")
	    private BigDecimal lossEstimateOsOc ;

	    @Column(name="EXCHANGE_RATE")
	    private BigDecimal exchangeRate ;

	    @Column(name="LOSS_ESTIMATE_OS_DC")
	    private BigDecimal lossEstimateOsDc ;

	    @Column(name="ADVICE_UW", length=10)
	    private String     adviceUw ;

	    @Column(name="ADVICE_MANAGEMENT", length=10)
	    private String     adviceManagement ;

	    @Column(name="RI_RECOVERY", length=10)
	    private String     riRecovery ;

	    @Column(name="RI_RECOVERY_AMOUNT_DC")
	    private BigDecimal riRecoveryAmountDc ;

	    @Column(name="RECOVERY_FROM", length=30)
	    private String     recoveryFrom ;

	    @Column(name="CREATED_BY", length=30)
	    private String     createdBy ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="CREATED_DATE")
	    private Date       createdDate ;

	    @Column(name="MODIFIED_BY", length=30)
	    private String     modifiedBy ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="MODIFIED_DATE")
	    private Date       modifiedDate ;

	    @Column(name="UPDATED_BY", length=30)
	    private String     updatedBy ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="UPDATED_DATE")
	    private Date       updatedDate ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="REVIEW_DATE")
	    private Date       reviewDate ;

	    @Column(name="REVIEW_DONE_BY", length=30)
	    private String     reviewDoneBy ;

	    @Column(name="TOTAL_AMT_PAID_TILL_DATE")
	    private BigDecimal totalAmtPaidTillDate ;

	    @Column(name="AMEND_ID")
	    private BigDecimal amendId ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="INCEPTION_DATE")
	    private Date       inceptionDate ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="EXPIRY_DATE")
	    private Date       expiryDate ;

	    @Column(name="REMARKS", length=500)
	    private String     remarks ;

	    @Column(name="STATUS", length=1)
	    private String     status ;

	    @Column(name="UW_MONTH")
	    private BigDecimal uwMonth ;

	    @Column(name="UW_YEAR")
	    private BigDecimal uwYear ;

	    @Column(name="CURRENCY", length=100)
	    private String     currency ;

	    @Column(name="ADVICE_UW_EMAILID", length=200)
	    private String     adviceUwEmailid ;

	    @Column(name="ADVICE_MGT_EMAILID", length=200)
	    private String     adviceMgtEmailid ;

	    @Column(name="RISK_CODE", length=30)
	    private String     riskCode ;

	    @Column(name="ACCUMULATION_CODE", length=30)
	    private String     accumulationCode ;

	    @Column(name="EVENT_CODE", length=30)
	    private String     eventCode ;

	    @Column(name="ALLOCATED_TILL_DATE")
	    private BigDecimal allocatedTillDate ;

	    @Column(name="ACC_CLAIM", length=30)
	    private String     accClaim ;

	    @Column(name="CHECKYN", length=1)
	    private String     checkyn ;

	    @Column(name="INSURED_NAME", length=200)
	    private String     insuredName ;

	    @Column(name="SUB_CLASS")
	    private BigDecimal subClass ;

	    @Column(name="BRANCH_CODE", length=20)
	    private String     branchCode ;

	    @Column(name="LOGIN_ID", length=100)
	    private String     loginId ;

	    @Column(name="RECORD_FEES_CRE_RESERVE", length=100)
	    private String     recordFeesCreReserve ;

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

	    @Column(name="RECORD_IBNR", length=100)
	    private String     recordIbnr ;

	    @Column(name="CEDENT_CLAIM_NO", length=100)
	    private String     cedentClaimNo ;

	    @Column(name="SAF_OS_DC", length=100)
	    private String     safOsDc ;

	    @Column(name="OTH_FEE_OS_DC", length=100)
	    private String     othFeeOsDc ;

	    @Column(name="C_IBNR_OS_DC", length=100)
	    private String     cIbnrOsDc ;

	    @Column(name="LOSS_ESTIMATE_DC", length=100)
	    private String     lossEstimateDc ;

	    @Column(name="LOSS_ESTIMATE_LAYER_OC")
	    private BigDecimal lossEstimateLayerOc ;

	    @Column(name="LOSS_ESTIMATE_LAYER_OS_OC")
	    private BigDecimal lossEstimateLayerOsOc ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="REOPENED_DATE")
	    private Date       reopenedDate ;

	    @Column(name="C_IBNR_OS_OC")
	    private BigDecimal cIbnrOsOc ;

	    @Column(name="SAF_OS_OC")
	    private BigDecimal safOsOc ;

	    @Column(name="OTH_FEE_OS_OC")
	    private BigDecimal othFeeOsOc ;

	    @Column(name="GROSSLOSS_FGU_OC")
	    private BigDecimal grosslossFguOc ;

	    @Column(name="CLAIM_CLASS", length=100)
	    private String     claimClass ;

	    @Column(name="CLAIM_SUBCLASS", length=500)
	    private String     claimSubclass ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="RES_POS_DATE")
	    private Date       resPosDate ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="CLAIM_CLOSED_DATE")
	    private Date       claimClosedDate ;

	    @Column(name="COVER_LIMIT_DEPTID", length=20)
	    private String     coverLimitDeptid ;

	    @Column(name="PROPOSAL_NO")
	    private BigDecimal proposalNo ;

	    @Column(name="PRODUCT_ID")
	    private BigDecimal productId ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="REPUDATE_DATE")
	    private Date       repudateDate ;


	    //--- ENTITY LINKS ( RELATIONSHIP )


	}




