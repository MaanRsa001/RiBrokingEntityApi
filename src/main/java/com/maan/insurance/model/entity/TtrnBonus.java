package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
@IdClass(TtrnBonusId.class)
@Table(name="TTRN_BONUS")

public class TtrnBonus implements Serializable{
	private static final long serialVersionUID = 1L;
	 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="SNO", nullable=false)
    private BigDecimal sno ;
    
    @Id
    @Column(name="BRANCH", nullable=false)
    private String     branch;
    
    //--- ENTITY DATA FIELDS 
    
    @Column(name="PROPOSAL_NO")
    private BigDecimal proposalNo ;
    
    @Column(name="CONTRACT_NO")
    private BigDecimal contractNo ;
    
    @Column(name="ENDORSEMENT_NO")
    private BigDecimal endorsementNo ;
    
    @Column(name="PRODUCT_ID")
    private String     productId ;
    
    @Column(name="TYPE")
    private String     type ;
    

    @Column(name="LCB_FROM")
    private String lcbFrom ;

    @Column(name="LCB_TO")
    private String     lcbTo ;


    @Column(name="LCB_PERCENTAGE")
    private String     lcbPercentage ;

    @Column(name="USERID")
    private String     userId ;


//    @Column(name="BRACH")
//    private String     brach ;

    
    @Column(name="LCB_ID")
    private String     lcbId ;
    
    @Column(name="LCB_TYPE")
    private String     lcbType ;
    
    @Column(name="SUB_CLASS")
    private String     subClass;
    
    @Column(name="LAYERNO")
    private String     layerNo ;
    
    @Column(name="QUOTA_SHARE")
    private String     quotaShare ;
    
    @Column(name="REMARKS")
    private String     remarks ;
    
    @Column(name="FIRST_PROFIT_COMM")
    private String     firstProfitComm ;
    
    @Column(name="FPC_DURATION_TYPE")
    private String     fpcDurationType ;
    
    @Column(name="SPC_DURATION_TYPE")
    private String     spcDurationType ;
    
    @Column(name="SUB_PROFIT_COMM")
    private String     subProfitComm ;
    
    @Column(name="SUB_SEC_CAL")
    private String     subSecCal ;
    
    @Column(name="SYS_DATE")
    private Date    sysDate ;
    
    @Column(name="REFERENCE_NO")
    private BigDecimal    referenceNo ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FPC_FIXED_DATE")
    private Date    fpcFixedDate ;
    
    @Column(name="SCALE_MAX_PART_PERCENT")
    private BigDecimal    scaleMaxPartPercent ;
    
    @Column(name="FPC_TYPE")
    private String     fpcType ; 
    
    //  Ri
    @Column(name="PROVISIONAL_COMMISIION")
    private BigDecimal     provisionalCommisiion ;
    
    @Column(name="SC_METHOD_TYPE")
    private String     scMethodType ;
    
    @Column(name="SC_MAX_LOSS_RATIO")
    private BigDecimal     scMaxLossRatio ;
    
    @Column(name="SC_MIN_LOSS_RATIO")
    private BigDecimal     scMinLossRatio ;
    
    @Column(name="SC_COMBINE_LOSS_RATIO")
    private BigDecimal     scCombineLossRatio ;
    
    @Column(name="SC_BANDING_STEP")
    private BigDecimal     scBandingStep ;
    
    @Column(name="SC_NO_OF_DIGIT")
    private BigDecimal     scNoOfDigit ;
    
    @Column(name="DELTA_LOSS_RATIO")
    private BigDecimal     deltaLossRatio ;
  
}
