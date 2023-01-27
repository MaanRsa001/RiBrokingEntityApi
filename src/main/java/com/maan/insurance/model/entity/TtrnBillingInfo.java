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
@IdClass(TtrnBillingInfoId.class)
@Table(name="TTRN_BILLING_INFO")
public class TtrnBillingInfo implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="BILLING_NO", nullable=false)
	    private BigDecimal     billingNo ;

	    @Id
	    @Column(name="AMEND_ID", nullable=false)
	    private BigDecimal amendId ;
	    
	    @Id
	    @Column(name="TRANS_TYPE", nullable=false)
	    private String     transType ;

	    @Id
	    @Column(name="BRANCH_CODE", nullable=false)
	    private String branchCode ;

	    //--- ENTITY DATA FIELDS 
	    @Column(name="BROKER_ID")
	    private BigDecimal brokerId ;

	    @Column(name="CEDING_ID")
	    private BigDecimal     cedingId ;

	    @Column(name="PRODUCT_ID")
	    private BigDecimal     productId ;
	    
	    @Column(name="CURRENCY_ID")
	    private BigDecimal currencyId ;
	    
	    @Column(name="ROUNDING_AMOUNT")
	    private BigDecimal roundingAmt ;
	    
	    @Column(name="UTILIZED_TILL_DATE")
	    private BigDecimal utilizedTillDate ;
	    
	    @Column(name="REVERSALTRANSNO")
	    private BigDecimal     reversaltransno ;

	    @Column(name="REVERSELLOGINID")
	    private String     reverselloginid ;

	    @Column(name="LOGIN_ID")
	    private String     loginId ;

	    @Column(name="REMARKS")
	    private String     remarks ;

	    @Column(name="REVTRANSALTYPE")
	    private String     revtransaltype ;

	    @Column(name="STATUS")
	    private String     status ;

	    @Column(name="TRANSCATIONTYPE")
	    private String     transcationtype ;
	    
	    @Column(name="ROUNDING_AMOUNT")
	    private BigDecimal     roundingAmount ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="SYS_DATE")
	    private Date       sysDate ;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="AMENDMENT_DATE")
	    private Date       amendmentDate ;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="BILL_DATE")
	    private Date       billDate ;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="REVERSALDATE")
	    private Date       reversaldate ;

}
