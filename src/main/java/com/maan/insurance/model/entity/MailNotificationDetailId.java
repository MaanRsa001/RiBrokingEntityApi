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
public class MailNotificationDetailId implements Serializable {

    private static final long serialVersionUID = 1L;

    //--- ENTITY KEY ATTRIBUTES 
    private BigDecimal mailRecordNo ;
    
    private String     mailType ;
    
    private BigDecimal proposalNo ;
    
    private BigDecimal sno ;
    
    private String     branchCode ;
    
}
