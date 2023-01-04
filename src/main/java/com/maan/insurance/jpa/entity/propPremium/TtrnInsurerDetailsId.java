/*
 * Created on 2022-09-15 ( 15:43:33 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */
package com.maan.insurance.jpa.entity.propPremium;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


import java.math.BigDecimal;

/**
 * Composite primary key for entity "TtrnInsurerDetails" ( stored in table "TTRN_INSURER_DETAILS" )
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
public class TtrnInsurerDetailsId implements Serializable {

    private static final long serialVersionUID = 1L;

    //--- ENTITY KEY ATTRIBUTES 
    private BigDecimal insurerNo ;
    
    private String     proposalNo ;
    
    private BigDecimal endorsementNo ;
    
     
}
