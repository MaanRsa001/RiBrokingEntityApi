package com.maan.insurance.jpa.entity.facultative;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
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
@Table(name="TTRN_LOSS_Details")
public class TtrnLossDetails {

	@Id
    @Column(name="PROPOSAL_NO")
	private String proposalNo;
	
	@Column(name="CONTRACT_NO")
	private String contractNo;
	
	@Column(name="ENDORSEMENT_NO")
	private String endorsementNo;
	
	@Column(name="PRODUCT_ID")
	private String productId;
	
	@Column(name="BRANCH")
	private String branch;
	
	@Column(name="SYS_DATE")
	private Date sysDate;
	
	@Column(name="LAYER_NO")
	private Integer layerNo;
	
	@Column(name="YEAR")
	private String year;
	
	@Column(name="LOSS_NO")
	private String lossNo;
	
	@Column(name="INSURED_NAME")
	private String insuredName;
	
	@Column(name="INCEPTION_DATE")
	private Date inceptionDate;
	
	@Column(name="EXPIRYDATE")
	private Date expirydate;
	
	@Column(name="DATE_OF_LOSS")
	private Date dateOfLoss;
	
	@Column(name="CAUSE_OF_LOSS")
	private String causeOfLoss;
	
	@Column(name="INSURED_CLAIM")
	private BigDecimal insuredClaim;
	
	@Column(name="PREMIUM")
	private BigDecimal premium;
	
	@Column(name="LOSS_RATIO")
	private BigDecimal lossRatio;
	
	@Column(name="LEADER")
	private String leader;
	
	@Column(name="ITI_RE_SHARE")
	private BigDecimal itiReShare;
	
}
