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
@IdClass(ExchangeMasterId.class)
@Table(name="EXCHANGE_MASTER")
public class ExchangeMaster  implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="EXCHANGE_ID", nullable=false)
	    private BigDecimal exchangeId ;

	    @Id
	    @Column(name="AMEND_ID", nullable=false)
	    private BigDecimal amendId ;

	    @Id
	    @Column(name="COUNTRY_ID", nullable=false)
	    private BigDecimal countryId ;
	   

	    //--- ENTITY DATA FIELDS 
	    
	    @Column(name="BRANCH_CODE", nullable=false, length=8)
	    private String     branchCode ;
	    @Column(name="CORE_APP_CODE", length=10)
	    private String     coreAppCode ;
	    
	    @Column(name="CURRENCY_ID")
	    private BigDecimal     currencyId ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="INCEPTION_DATE")
	    private Date       inceptionDate ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="EXPIRY_DATE")
	    private Date       expiryDate ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="EFFECTIVE_DATE")
	    private Date       effectiveDate ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="ENTRY_DATE")
	    private Date       entryDate ;

	    @Column(name="REMARKS", length=100)
	    private String     remarks ;

	    @Column(name="STATUS", length=1)
	    private String     status ;



	    @Column(name="DISPLAY_ORDER")
	    private BigDecimal     displayOrder ;

	    @Column(name="SNO")
	    private BigDecimal sno ;

	    @Column(name="EXCHANGE_RATE")
	    private BigDecimal exchangeRate ;


	    //--- ENTITY LINKS ( RELATIONSHIP )


	}


