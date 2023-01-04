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
@Table(name="Final_Jv_Format")
public class FinalJvFormat implements Serializable{ 
	private static final long serialVersionUID = 1L;
	 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="JOURNAL_ID", nullable=false)
    private BigDecimal journalId ;
    
    //--- ENTITY DATA FIELDS 
    @Column(name="SERIAL_NO")
    private BigDecimal serialNo ;
    
    @Column(name="REFERENCE")
    private String reference ;
   
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="STARTDATE")
    private Date     startDate ;
    
    @Column(name="VOUCHERSUBTYPE")
    private String     voucherSubtype ;
    

    @Column(name="VOUCHERTYPE")
    private String voucherType ;

    @Column(name="LEDGER")
    private String     ledger ;


    @Column(name="D_C")
    private String     dc ;

    @Column(name="DEBITOC")
    private String     debitoc ;

    @Column(name="CREDITOC")
    private String     creditoc ;
    
    @Column(name="DEBITUGX")
    private String     debitugx;
    
    @Column(name="CREDITUGX")
    private String     creditugx ;
    
    @Column(name="POSTING_DEBIT")
    private String     postingDebit ;
    
    @Column(name="POSTING_CREDIT")
    private String     postingCredit;
    
    @Column(name="CURRENCYSYMBOL")
    private String     currencySymbol ;
    
    @Column(name="EXCHANGE_RATE")
    private String     exchangeRate ;
    
    @Column(name="UWY")
    private String     uwy ;
    
    @Column(name="SPC")
    private String     spc ;
    
    @Column(name="CURRENCY")
    private String     currency ;
    
    @Column(name="NARRATION")
    private String     narration ;
    
    @Column(name="BASE_CURRENCY")
    private String     baseCurrency ;
    
    @Column(name="PRODUCT_ID")
    private String     productId ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="END_DATE")
    private Date    endDate ;
    
    @Column(name="JV_TYPE")
    private String     jvType ;
    
    @Column(name="JV_ID")
    private String     jvId ;
    
    @Column(name="TRANSACTION_NO")
    private String     transactionNo ;
    
    @Column(name="REVERSAL_STATUS")
    private String     reversalStatus ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENTRY_DATE")
    private Date    entryDate ;
    
    @Column(name="POSTED")
    private String     posted ;
    
    @Column(name="JV_MONTH")
    private String     jvMonth ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="JV_DATE")
    private Date    jvDate ;
    
    @Column(name="BRANCH_CODE")
    private String     branchCode ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LOGIC_DATE")
    private Date    logicDate ;

}

