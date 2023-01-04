/*
 * Created on 2022-09-15 ( 15:43:28 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */
package com.maan.insurance.jpa.keys;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Composite primary key for entity "TtrnFacRiskProposal" ( stored in table "TTRN_FAC_RISK_PROPOSAL" )
 *
 * @author Telosys
 *
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TtrnFacRiskProposalKey implements Serializable {

    private static final long serialVersionUID = 1L;

    //--- ENTITY KEY ATTRIBUTES 
    private BigDecimal rskProposalNumber ;
    
    private BigDecimal rskEndorsementNo ;
    
     
}
