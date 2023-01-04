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
public class TtrnRiPlacementStatusId implements Serializable {

    private static final long serialVersionUID = 1L;

    //--- ENTITY KEY ATTRIBUTES 
    private String     brokerId ;
    
    private String reinsurerId ;
    
    private BigDecimal statusNo ;
    
    private BigDecimal sno ;
    
    private BigDecimal     correspondentId ;
    
    private BigDecimal proposalNo ;
    
    private String branchCode ;
    
}
