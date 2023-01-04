package com.maan.insurance.jpa.entity.treasury;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.maan.insurance.jpa.keys.TtrnPaymentReceiptDetailsKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TTRN_PAYMENT_RECEIPT_DETAILS")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@IdClass(TtrnPaymentReceiptDetailsKey.class)
public class TtrnPaymentReceiptDetails {

	@Id
	@Column(name = "RECEIPT_NO")
	private BigDecimal receiptNo;

	@Id
	@Column(name = "RECEIPT_SL_NO")
	private BigDecimal receiptSlNo;

	@Id
	@Column(name = "AMEND_ID")
	private BigDecimal amendId;

	@Column(name = "CURRENCY_ID")
	private BigDecimal currencyId;

	@Column(name = "AMOUNT")
	private BigDecimal amount;

	@Column(name = "EXCHANGE_RATE")
	private BigDecimal exchangeRate;

	@Column(name = "TRANS_DATE")
	private Timestamp transDate;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "ALLOCATED_TILL_DATE")
	private BigDecimal allocatedTillDate;

	@Column(name = "TOT_AMT")
	private BigDecimal totAmt;

	@Column(name = "SETTLED_EXCRATE")
	private BigDecimal settledExcrate;

	@Column(name = "CONVERTED_RECCUR")
	private BigDecimal convertedReccur;

	@Column(name = "LOGIN_ID")
	private String loginId;

	@Column(name = "SYS_DATE")
	private Date sysDate;

	@Column(name = "BRANCH_CODE")
	private String branchCode;

	public TtrnPaymentReceiptDetails(BigDecimal currencyId, BigDecimal exchangeRate, BigDecimal amount, BigDecimal totAmt,
			BigDecimal receiptNo, BigDecimal settledExcrate, BigDecimal convertedReccur) {
		super();
		this.receiptNo = receiptNo;
		this.currencyId = currencyId;
		this.amount = amount;
		this.exchangeRate = exchangeRate;
		this.totAmt = totAmt;
		this.settledExcrate = settledExcrate;
		this.convertedReccur = convertedReccur;
	}

}
