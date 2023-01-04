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
@Table(name="TTRN_RETRO_CLAIM_UPDATION")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TtrnRetroClaimUpdation implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name ="SL_NO")
	private BigDecimal slNo;
	
	@Column(name ="CLAIM_NO")
	private String claimNo;
	
	@Column(name ="CONTRACT_NO")
	private String contractNo;
	
	@Column(name ="LAYER_NO")
	private String layerNo;
	
	@Column(name ="LOSS_ESTIMATE_REVISED_OC")
	private String lossEstimateRevisedOc;
	
	@Column(name ="LOSS_ESTIMATE_REVISED_DC")
	private String lossEstimateRevisedDc;
	
	@Column(name ="UPDATE_REFERENCE")
	private String updateReference;
	
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name ="INCEPTION_DATE")
	private Date inceptionDate;
    
	@Column(name ="REMARKS")
	private String remarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name ="EXPIRY_DATE")
	private Date expiryDate;
	
	@Column(name ="STATUS")
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name ="SYS_DATE")
	private Date sysDate;
	
	@Column(name ="BRANCH_CODE")
	private String branchCode;
	
	@Column(name ="LOGIN_ID")
	private String loginId;
	
	@Column(name ="SAF_100_OC")
	private String Saf100oc;
	
	@Column(name ="SAF_100_DC")
	private BigDecimal saf100dc;
	
	@Column(name ="OTH_FEE_100_OC")
	private String othfee100oc;
	
	@Column(name ="OTH_FEE_100_DC")
	private String othfee100dc;
	
	@Column(name ="C_IBNR_100_OC")
	private String cIbnr100oc;
	
	@Column(name ="C_IBNR_100_DC")
	private String cIbnr100dc;
	
	@Column(name ="SAF_OS_DC")
	private String safosdc;
	
	@Column(name ="OTH_FEE_OS_DC")
	private String othFeeosdc;
	
	@Column(name ="C_IBNR_OS_DC")
	private String cIbnrosdc;
	
	@Column(name ="LOSS_ESTIMATE_100_OC")
	private String lossEstimate100oc;
	
	@Column(name ="LOSS_ESTIMATE_100_DC")
	private String lossEstimate100dc;
	
	@Column(name ="RESERVE_FEES")
	private String reserveFees;
	
	@Column(name ="RESERVE_IBNR")
	private String reserveIbnr;
	
	@Column(name ="SAF_OS_OC")
	private String safOsoc;
	
//	@Column(name ="SAF_OS_DC")
//	private String safOsdc;
	
	@Column(name ="OTH_FEE_OS_OC")
	private String othFeeOsoc;
	
	@Column(name ="C_IBNR_OS_OC")
	private String cibnrosoc;
	
	@Column(name ="TOT_RES_AMOUNT_OC")
	private String totresAmountoc;
	
	@Column(name ="TOT_RES_AMOUNT_DC")
	private String totResAmountdc;
	
	@Column(name ="EXCHANGE_RATE")
	private String exchangeRate;
	
	@Column(name ="RES_POS_DATE")
	private String resposdate;
	

}
