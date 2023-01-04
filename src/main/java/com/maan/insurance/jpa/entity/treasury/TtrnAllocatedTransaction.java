package com.maan.insurance.jpa.entity.treasury;

import java.math.BigDecimal;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.maan.insurance.jpa.keys.TtrnAllocatedTranasctionKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="TTRN_ALLOCATED_TRANSACTION")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@IdClass(TtrnAllocatedTranasctionKey.class)
public class TtrnAllocatedTransaction {
	
	@Id
	@Column(name ="SNO")
	private BigDecimal sno;
	
	@Column(name ="CONTRACT_NO")
	private String contractNo;
	
	@Column(name ="LAYER_NO")
	private String layerNo;
	
	@Column(name ="PRODUCT_NAME")
	private String productName;
	
	@Column(name ="CURRENCY_ID")
	private BigDecimal currencyId;
	
	@Column(name ="RECEIPT_NO")
	private BigDecimal receiptNo;
	
	@Id
	@Column(name ="TRANSACTION_NO")
	private BigDecimal transactionNo;
	
	@Column(name ="TYPE")
	private String type;
	
	@Column(name ="PAID_AMOUNT")
	private BigDecimal paidAmount;
	
	@Column(name ="AMEND_ID")
	private BigDecimal amendId;
	
	@Column(name ="INCEPTION_DATE")
	private Date inceptionDate;
	
	@Column(name ="STATUS")
	private String status;
	
	@Column(name ="REMARKS")
	private String remarks;
	
	@Column(name ="REVERSAL_DATE")
	private Date reversalDate;
	
	@Column(name ="REVERSAL_AMOUNT")
	private BigDecimal reversalAmount;
	
	@Column(name ="ADJUSTMENT_TYPE")
	private String adjustmentType;
	
	@Column(name ="PROCESS_TYPE")
	private String processType;
	
	@Column(name ="SUB_CLASS")
	private String subClass;
	
	@Column(name ="LOGIN_ID")
	private String loginId;
	
	@Column(name ="SYS_DATE")
	private Date sysDate;
	
	@Column(name ="BRANCH_CODE")
	private String branchCode;
	
	@Column(name ="PROPOSAL_NO")
	private String proposalNo;
	
	@Column(name ="MULTIPLE_CLIENT_YN")
	private String multipleClientYn;
	
}
