package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




/**
* Domain class for entity "TmasProductMaster"
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
@IdClass(TmasProductMasterKey.class)
@Table(name="TMAS_PRODUCT_MASTER")
public class TmasProductMaster implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="TMAS_PRODUCT_ID", nullable=false)
    private BigDecimal tmasProductId ;

    @Id
    @Column(name="BRANCH_CODE", nullable=false, length=8)
    private String     branchCode ;

    //--- ENTITY DATA FIELDS 
    @Column(name="TMAS_PRODUCT_NAME", length=100)
    private String     tmasProductName ;

    @Column(name="TMAS_STATUS", length=1)
    private String     tmasStatus ;

    @Column(name="CORE_COMPANY_CODE", length=10)
    private String     coreCompanyCode ;


    //--- ENTITY LINKS ( RELATIONSHIP )


}



