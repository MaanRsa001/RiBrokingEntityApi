package com.maan.insurance.jpa.entity.treasury;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="TTRN_PAYMENT_RECEIPT")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TtrnPaymentReceipt {
	
	@Id
	@Column(name ="PAYMENT_RECEIPT_NO")
	private Long paymentReceiptNo;
	
	@Column(name ="TRANS_TYPE")
	private String transType;
	
	@Column(name ="CEDING_ID")
	private Integer cedingId;
	
	@Column(name ="BROKER_ID")
	private Integer brokerId;
	
	@Column(name ="TRANS_DATE")
	private Date transDate;
	
	@Column(name ="TRANS_MONTH")
	private Integer transMonth;
	
	@Column(name ="TRANS_YEAR")
	private Integer transYear;
	
	@Column(name ="CURRENCY_ID")
	private Integer currencyId;
	
	@Column(name ="EXCHANGE_RATE")
	private Double exchangeRate;
	
	@Column(name ="RECEIVED_AMT")
	private Integer receivedAmt;
	
	@Column(name ="PAID_AMT")
	private Double paidAmt;
	
	@Column(name ="REMARKS")
	private String remarks;
	
	@Column(name ="STATUS")
	private String status;
	
	@Column(name ="PRODUCT_ID")
	private Integer productId;
	
	@Column(name ="DEPT_ID")
	private Integer deptId;
	
	@Column(name ="SERIAL_NO")
	private Integer serialNo;
	
	@Column(name ="PAYMENT_RESPONSE")
	private String paymentResponse;
	
	@Column(name ="RECEIPT_BANK")
	private Integer receiptBank;
	
	@Column(name ="DIFF_AMT")
	private Double diffAmt;
	
	@Column(name ="TRANSCATIONTYPE")
	private String transactionType;
	
	@Column(name ="REVTRANSALTYPE")
	private String revtransalType;
	
	@Column(name ="REVERSALTRANSNO")
	private Integer reversalTransNo;
	
	@Column(name ="REVERSALDATE")
	private Date reversalDate;
	
	@Column(name ="REVERSELLOGINID")
	private String reversalLoginId;
	
	@Column(name ="BRANCH_CODE")
	private String branchCode;
	
	@Column(name ="BANK_CHARGES")
	private Double bankCharges;
	
	@Column(name ="NET_AMT")
	private Double netAmt;
	
	@Column(name ="AMENDMENT_DATE")
	private Date amendmentDate;
	
	@Column(name ="WH_TAX")
	private Double whTax;
	
	@Column(name ="AMEND_ID")
	private Integer amendId;
	
	@Column(name ="CONVERTED_DIFF_AMT")
	private Double convertedDiffAmt;
	
	@Column(name ="LOGIN_ID")
	private String loginId;
	
	@Column(name ="SYS_DATE")
	private Date sysDate;
	
	@Column(name ="PREMIUM_LAVY")
	private Double premiumLavy;
	
}
