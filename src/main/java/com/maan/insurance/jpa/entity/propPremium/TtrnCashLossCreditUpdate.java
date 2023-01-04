package com.maan.insurance.jpa.entity.propPremium;

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

import com.maan.insurance.jpa.keys.TtrnCashLossCreditUpdateKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




/**
* Domain class for entity "TtrnCashLossCreditUpdate"
*
* @author Telosys Tools Generator
*
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@IdClass(TtrnCashLossCreditUpdateKey.class)
@Table(name="TTRN_CASH_LOSS_CREDIT_UPDATE")


public class TtrnCashLossCreditUpdate implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="CONTRACT_NO", nullable=false, length=20)
    private String     contractNo ;

    @Id
    @Column(name="TRANSACTION_NO", nullable=false)
    private BigDecimal transactionNo ;

    @Id
    @Column(name="CLAIM_NO", nullable=false)
    private BigDecimal claimNo ;

    @Id
    @Column(name="AMEND_ID", nullable=false)
    private BigDecimal amendId ;

    //--- ENTITY DATA FIELDS 
    @Column(name="CURRENCY_ID")
    private BigDecimal currencyId ;

    @Column(name="EXCHANGE_RATE")
    private BigDecimal exchangeRate ;

    @Column(name="CASH_LOSS_CREDIT_OC")
    private BigDecimal cashLossCreditOc ;

    @Column(name="CASH_LOSS_CREDIT_DC")
    private BigDecimal cashLossCreditDc ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENTRY_DATE")
    private Date       entryDate ;

    @Column(name="STATUS", length=2)
    private String     status ;

    @Column(name="REMARKS", length=150)
    private String     remarks ;


    //--- ENTITY LINKS ( RELATIONSHIP )


}



