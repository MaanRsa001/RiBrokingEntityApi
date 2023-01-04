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
public class TtrnRiPlacementId implements Serializable {

    private static final long serialVersionUID = 1L;

    //--- ENTITY KEY ATTRIBUTES 
    private BigDecimal     placementNo ;
    
    private BigDecimal statusNo ;
    
    private BigDecimal sno ;
    
    private BigDecimal proposalNo ;
    
    private String branchCode ;
    
     
}

