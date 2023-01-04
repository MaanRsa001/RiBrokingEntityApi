package com.maan.insurance.jpa.entity.claim;

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
import lombok.ToString;

/**
 * Domain class for entity "TmasProductMaster"
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
@Table(name = "TTRN_CLAIM_PAYMENT_ARCHIVE")
public class TtrnClaimPaymentArchive implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SL_NO")
	private BigDecimal slNo;

	@Column(name = "CLAIM_NO")
	private BigDecimal claimNo;

	@Column(name = "CONTRACT_NO")
	private String contractNo;

	@Column(name = "LAYER_NO")
	private BigDecimal layerNo;

	@Column(name = "PAYMENT_REQUEST_NO")
	private String paymentRequestNo;

	@Column(name = "PAID_AMOUNT_OC")
	private BigDecimal paidAmountOc;

	@Column(name = "PAID_AMOUNT_DC")
	private BigDecimal paidAmountDc;

	@Column(name = "LOSS_ESTIMATE_REVISED_OC")
	private BigDecimal lossEstimateRevisedOc;

	@Column(name = "LOSS_ESTIMATE_REVISED_DC")
	private BigDecimal lossEstimateRevisedDc;

	@Column(name = "CLAIM_NOTE_RECOMM")
	private String claimNoteRecomm;

	@Column(name = "PAYMENT_REFERENCE")
	private String paymentReference;

	@Column(name = "ADVICE_TREASURY")
	private String adviceTreasury;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="INCEPTION_DATE")
    private Date       inceptionDate ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EXPIRY_DATE")
    private Date       expiryDate ;
    
	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "CLAIM_PAYMENT_NO")
	private Long claimPaymentNo;

	@Column(name = "ADVICE_TREASURY_EMAILID")
	private String adviceTreasuryEmailId;

	@Column(name = "ALLOCATED_TILL_DATE")
	private BigDecimal allocatedTillDate;

	@Column(name = "ACC_CLAIM")
	private String accClaim;

	@Column(name = "CHECKYN")
	private String checkyn;

	@Column(name = "RESERVE_ID")
	private BigDecimal reserveId;

	@Column(name = "SETTLEMENT_STATUS")
	private String settlementStatus;

	@Column(name = "REINSTATEMENT_TYPE")
	private String reinstatementType;

	@Column(name = "REINSPREMIUM_OURSHARE_OC")
	private BigDecimal reinspremiumOurshareOc;

	@Column(name = "REINSPREMIUM_OURSHARE_DC")
	private BigDecimal reinspremiumOurshareDc;

	@Column(name = "NETCLAIMAMT_OURSHARE_OC")
	private BigDecimal netclaimamtOurshareOc;

	@Column(name = "NETCLAIMAMT_OURSHARE_DC")
	private BigDecimal netclaimamtOurshareDc;

	@Column(name = "EXCHANGE_RATE")
	private BigDecimal exchangeRate;

	@Column(name = "INSURED_NAME")
	private String insuredName;

	@Column(name = "CASH_LOSS_SETTLED_TILLDATE")
	private BigDecimal cashLossSettledTilldate;

	@Column(name = "BRANCH_CODE")
	private String branchCode;

	@Column(name = "LOGIN_ID")
	private String loginId;

	@Column(name = "PAID_CLAIM_OS_OC")
	private BigDecimal paidClaimOsOc;

	@Column(name = "PAID_CLAIM_OS_DC")
	private BigDecimal paidClaimOsDc;

	@Column(name = "SAF_OS_OC")
	private BigDecimal safOsOc;

	@Column(name = "SAF_OS_DC")
	private BigDecimal safOsDc;

	@Column(name = "OTH_FEE_OS_OC")
	private BigDecimal othFeeOsOc;

	@Column(name = "OTH_FEE_OS_DC")
	private BigDecimal othFeeOsDc;

	@Column(name = "SYS_DATE")
	private Date sysDate;

	@Column(name = "AMEND_ID")
	private Long amendId;

}
