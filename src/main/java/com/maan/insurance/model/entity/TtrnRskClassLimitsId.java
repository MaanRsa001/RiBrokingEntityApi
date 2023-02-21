/*
 * Created on 2022-09-15 ( 15:44:16 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */
package com.maan.insurance.model.entity;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


import java.math.BigDecimal;

/**
 * Composite primary key for entity "TtrnRiskProposal" ( stored in table "TTRN_RISK_PROPOSAL" )
 *
 * @author Telosys
 *
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TtrnRskClassLimitsId implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal rskProposalNumber ;
    
    private BigDecimal     rskSno ;
    
     
}
