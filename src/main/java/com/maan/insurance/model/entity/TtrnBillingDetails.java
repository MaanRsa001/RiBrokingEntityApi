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
@IdClass(TtrnBillingDetailsId.class)
@Table(name="TTRN_BILLING_DETAILS")
public class TtrnBillingDetails implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="BILLING_NO", nullable=false)
	    private BigDecimal     billingNo ;

	    @Id
	    @Column(name="AMEND_ID", nullable=false)
	    private BigDecimal amendId ;
	    
	    @Id
	    @Column(name="BILLING_SL_NO", nullable=false)
	    private BigDecimal     billingSlNo ;

	    @Id
	    @Column(name="BRANCH_CODE", nullable=false)
	    private String branchCode ;

	    //--- ENTITY DATA FIELDS 
	    @Column(name="ALLOCATED_TILL_DATE")
	    private BigDecimal allocatedTillDate ;

	    @Column(name="AMOUNT")
	    private BigDecimal     amount ;

	    @Column(name="CONVERTED_RECCUR")
	    private BigDecimal     convertedReccur ;

	    @Column(name="CURRENCY_ID")
	    private BigDecimal     currencyId ;

	    @Column(name="EXCHANGE_RATE")
	    private BigDecimal     exchangeRate ;

	    @Column(name="LOGIN_ID")
	    private String     loginId ;

	    @Column(name="REMARKS")
	    private String     remarks ;

	    @Column(name="SETTLED_EXCRATE")
	    private BigDecimal     settledExcrate ;

	    @Column(name="STATUS")
	    private String     status ;

	    @Column(name="TOT_AMT")
	    private BigDecimal     totAmt ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="SYS_DATE")
	    private Date       sysDate ;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="TRANS_DATE")
	    private Date       transDate ;
}
