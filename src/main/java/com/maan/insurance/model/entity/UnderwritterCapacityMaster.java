package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

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
@Table(name="UNDERWRITER_CAPACITY_MASTER")
public class UnderwritterCapacityMaster implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="UNDERWRITERID", nullable=false)
	    private String     underwriterid ;

	 
	    @Column(name="BRANCH_CODE")
	    private String     branchCode ;

	    @Column(name="UNDERWRITTER", length=30)
	    private String     underwritter ;

	    @Column(name="DEPARTMENTID")
	    private String     departmentid ;

	    @Column(name="PRODUCT_ID")
	    private String     productId ;
	    
	    @Column(name="STATUS")
	    private String     status ;

	    @Column(name="UNDERWRITTER_LIMIT")
	    private BigDecimal     underwritterLimit ;


	    //--- ENTITY LINKS ( RELATIONSHIP )


	}



