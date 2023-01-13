package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class TtrnClaimPaymentRiId implements Serializable {

    private static final long serialVersionUID = 1L;

    //--- ENTITY KEY ATTRIBUTES 
    private BigDecimal slNo ;
    
    private BigDecimal claimNo ;
    
    private String     contractNo ;
    
    private BigDecimal contractAmendId ;
    
    private String reinsurerId ;
    
    private String     brokerId ;
    
    private BigDecimal statusNo ;
    
    private BigDecimal riTransactionNo ;
    
    private String     branchCode ;
}
