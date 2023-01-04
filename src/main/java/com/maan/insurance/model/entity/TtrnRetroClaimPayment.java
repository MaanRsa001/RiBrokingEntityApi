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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="TTRN_RETRO_CLAIM_PAYMENT")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TtrnRetroClaimPayment implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name ="SL_NO")
	private BigDecimal SL_NO;
	
	@Column(name ="CONTRACT_NO")
	private String contractNo;
	
	@Column(name ="LAYER_NO")
	private String layerNo;
	
	@Column(name ="PAYMENT_REQUEST_NO")
	private String paymentRequestNo;
	
	@Column(name ="PAID_AMOUNT_OC")
	private BigDecimal paidAmountOc;
	
	@Column(name ="CLAIM_NO")
	private String claimNo;
	
	@Column(name ="PAID_AMOUNT_DC")
	private String paidAmountDc;
	
	@Column(name ="LOSS_ESTIMATE_REVISED_OC")
	private String lossEstimateRevisedOc;
	
	@Column(name ="LOSS_ESTIMATE_REVISED_DC")
	private String lossEstimateRevisedDc;
	
	@Column(name ="CLAIM_NOTE_RECOMM")
	private String claimNoteRecomm;
	
	@Column(name ="PAYMENT_REFERENCE")
	private String paymentReference;
	
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name ="INCEPTION_DATE")
	private Date inceptionDate;
	
	@Column(name ="ADVICE_TREASURY")
	private String adviceTreasury;
	
	@Column(name ="REMARKS")
	private String remarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name ="EXPIRY_DATE")
	private Date expiryDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name ="SYS_DATE")
	private Date sysDate;
	 
	@Column(name ="STATUS")
	private String status;
	
	@Column(name ="PAYMENT_TYPE")
	private String paymentType;
	
	@Column(name ="CLAIM_PAYMENT_NO")
	private String claimPaymentNo;
	
	@Column(name ="ADVICE_TREASURY_EMAILID")
	private String adviceTreasuryEmailid;
	
	@Column(name ="ALLOCATED_TILL_DATE")
	private String allocatedTillDate;
	
	@Column(name ="ACC_CLAIM")
	private String accClaim;
	
	@Column(name ="CHECKYN")
	private String checkyn;
	
	@Column(name ="RESERVE_ID")
	private String reserveId;
	
	@Column(name ="SETTLEMENT_STATUS")
	private String settlementStatus;
	
	@Column(name ="REINSTATEMENT_TYPE")
	private String reinstatementType;
	
	@Column(name ="REINSPREMIUM_OURSHARE_OC")
	private String reinspremiumOurshareOc;
	
	@Column(name ="REINSPREMIUM_OURSHARE_DC")
	private String reinsPremiumOurShareDc;
	
	@Column(name ="NETCLAIMAMT_OURSHARE_OC")
	private String netClaimamtOurshareOc;
	
	@Column(name ="NETCLAIMAMT_OURSHARE_DC")
	private String netClaimamtOurshareDc;
	
	@Column(name ="EXCHANGE_RATE")
	private String exchangeRate;
	
	@Column(name ="INSURED_NAME")
	private String insuredName;
	
	@Column(name ="CASH_LOSS_SETTLED_TILLDATE")
	private String cashLossSettledTilldate;
	
	@Column(name ="BRANCH_CODE")
	private String branchCode;
	
	@Column(name ="LOGIN_ID")
	private String loginId;
	
	@Column(name ="PAID_CLAIM_OS_OC")
	private String paidClaimOsOc;
	
	@Column(name ="PAID_CLAIM_OS_DC")
	private String paidClaimOsDc;
	
	@Column(name ="SAF_OS_OC")
	private String safOsoc;
	
	@Column(name ="SAF_OS_DC")
	private String safOsdc;
	
	@Column(name ="OTH_FEE_OS_OC")
	private String othFeeOsoc;
	
	@Column(name ="OTH_FEE_OS_DC")
	private String othFeeOsdc;
}
