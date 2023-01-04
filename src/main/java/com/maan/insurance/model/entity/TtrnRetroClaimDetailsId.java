package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.maan.insurance.model.entity.TtrnClaimDetailsId;

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
public class TtrnRetroClaimDetailsId implements Serializable {

    private static final long serialVersionUID = 1L;

    //--- ENTITY KEY ATTRIBUTES 
    private BigDecimal claimNo ;
    
    private String     contractNo ;

}
