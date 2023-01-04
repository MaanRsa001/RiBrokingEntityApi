package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name="MENU_MASTER")
public class MenuMaster implements Serializable{ 
	private static final long serialVersionUID = 1L; 
	 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="MENU_ID", nullable=false)
    private BigDecimal menuId ;
    
    //--- ENTITY DATA FIELDS 
    @Column(name="PRODUCT_ID")
    private BigDecimal productId ;
    
    @Column(name="DEPT_ID")
    private BigDecimal deptId ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="END_DATE")
    private Date     endDate ;
    
    @Column(name="PROCESS_ID")
    private BigDecimal     processId ;
    

    @Column(name="MENU_NAME")
    private String menuName ;

    @Column(name="MENU_URL")
    private String     menuUrl ;


    @Column(name="MENU_TYPE")
    private String     menuType ;

    @Column(name="ORDER_NO")
    private BigDecimal     orderNo ;

    @Column(name="ACTIVE")
    private String     active ;
    
    @Column(name="REMARKS")
    private String     remarks;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="START_DATE")
    private Date    startDate ;
    
    @Column(name="TYPE")
    private String     type ;
    
    @Column(name="CED_APP_NUMBER")
    private BigDecimal     cedAppNumber ;
    
    @Column(name="SUB_MENU_CODES")
    private String     subMenuCodes;
    
   
}

