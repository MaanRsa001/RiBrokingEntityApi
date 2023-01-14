package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;

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
@Builder
public class RskPremiumDetailsRiId implements Serializable {

    private static final long serialVersionUID = 1L;

    //--- ENTITY KEY ATTRIBUTES 
    private BigDecimal transactionNo ;
    
    private BigDecimal amendId ;
    
    private String     branchCode ;
    
    private String     reinsurerId ;
    
    private String     brokerId ;
    
    private BigDecimal ritransactionNo ;
}
