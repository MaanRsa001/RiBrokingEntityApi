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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@IdClass(TtrnBillingTransactionId.class)
@Table(name="TTRN_BILLING_TRANSACTION")
public class TtrnBillingTransaction implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="BILL_NO", nullable=false)
	    private BigDecimal     billNo ;

	    @Id
	    @Column(name="AMEND_ID", nullable=false)
	    private BigDecimal amendId ;
	    
	    @Id
	    @Column(name="BILL_SNO", nullable=false)
	    private BigDecimal     billSno ;

	    @Id
	    @Column(name="BRANCH_CODE", nullable=false)
	    private String branchCode ;
	    
	    @Id
	    @Column(name="TRANSACTION_NO", nullable=false)
	    private String transactionNo ;

	    //--- ENTITY DATA FIELDS 
	    @Column(name="ADJUSTMENT_TYPE")
	    private String adjustmentType ;

	    @Column(name="CONTRACT_NO")
	    private BigDecimal     contractNo ;

	    @Column(name="CURRENCY_ID")
	    private BigDecimal     currencyId ;

	    @Column(name="LAYER_NO")
	    private BigDecimal     layerNo ;


	    @Column(name="LOGIN_ID")
	    private String     loginId ;

	    @Column(name="REMARKS")
	    private String     remarks ;

	    @Column(name="PAID_AMOUNT")
	    private BigDecimal     paidAmount ;

	    @Column(name="STATUS")
	    private String     status ;

	    @Column(name="PROCESS_TYPE")
	    private String     processType ;
	    
	    @Column(name="PRODUCT_NAME")
	    private String     productName ;

	    @Column(name="PROPOSAL_NO")
	    private BigDecimal     proposalNo ;
	    
	    @Column(name="REVERSAL_AMOUNT")
	    private BigDecimal     reversalAmount ;
	    
	    @Column(name="TYPE")
	    private String     type ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="SYS_DATE")
	    private Date       sysDate ;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="INCEPTION_DATE")
	    private Date       inceptionDate ;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="REVERSAL_DATE")
	    private Date       reversalDate ;

}
