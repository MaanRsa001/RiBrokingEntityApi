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
@Table(name="TTRN_MANUAL_JV")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TtrnManualJv implements Serializable{  
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SNO")
	private BigDecimal sno;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AMENDMENT_DATE")
	private Date amendmentDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENRTY_DATE")
	private Date entryDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SYS_DATE")
	private Date sysDate;
	
	@Column(name = "AMEND_ID")
	private BigDecimal amendId;
	
	@Column(name = "CLASS")
	private String clas;
	
	@Column(name = "CREDIT_DC")
	private String creditDc;
	
	@Column(name = "CREDIT_OC")
	private String creditOc;
	
	@Column(name = "BRANCH_CODE")
	private String branchCode;
	
	@Column(name = "CURRENCY")
	private String currency;
	
	@Column(name = "DEBIT_DC")
	private String debitDc;
	
	@Column(name = "DEBIT_OC")
	private String debitOc;
	
	@Column(name = "EXCHANGE_RATE")
	private String exchangeRate;
	
	@Column(name = "LEDGER")
	private String ledger;
	
	@Column(name = "LOGIN_ID")
	private String loginId;
	
	@Column(name = "NARRATION")
	private String narration;
	
	@Column(name = "REVERAL_NO")
	private String reveralNo;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "TRANSACTION_NO")
	private String transactionNo;
}
