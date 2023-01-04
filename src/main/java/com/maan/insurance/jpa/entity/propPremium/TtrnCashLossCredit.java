package com.maan.insurance.jpa.entity.propPremium;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
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
@Table(name="TTRN_CASH_LOSS_CREDIT")
public class TtrnCashLossCredit {

	@Id
	@Column(name="CLC_NO")
	private BigDecimal clcNo;
	
	@Column(name="CONTRACT_NO")
	private BigDecimal contractNo;
	
	@Column(name="CLAIM_NO")
	private BigDecimal claimNo;
	
	@Column(name="CLAIMPAYMENT_NO")
	private BigDecimal claimpaymentNo;
	
	@Column(name="CLCCURRENCY_ID")
	private BigDecimal clccurrencyId;
	
	@Column(name="CREDITAMOUNTCLC")
	private BigDecimal creditamountclc;
	
	@Column(name="CLDCURRENCY_ID")
	private BigDecimal cldcurrencyId;
	
	@Column(name="EXCHANGE_RATE")
	private BigDecimal exchangeRate;
	
	@Column(name="CREDITAMOUNTCLD")
	private BigDecimal creditamountcld;
	
	@Column(name="CREDITTRXNNO")
	private String credittrxnno;
	
	@Column(name="CREDITDATE")
	private Date creditdate;
	
	@Column(name="BRANCH_CODE")
	private String branchCode;
	
	@Column(name="TEMP_REQUEST_NO")
	private BigDecimal tempRequestNo;
	
	@Column(name="TABLE_MOVE_STATUS")
	private String tableMoveStatus;
	
	@Column(name="CLD_AMOUNT")
	private BigDecimal cldAmount;
	
	@Column(name="PROPOSAL_NO")
	private BigDecimal proposalNo;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="REVERSAL_CLC")
	private BigDecimal reversalClc;
	
	@Column(name="REVERSAL_CLD")
	private BigDecimal reversalCld;
	
	@Column(name="REVERSAL_DATE")
	private Date reversalDate;
	
}
