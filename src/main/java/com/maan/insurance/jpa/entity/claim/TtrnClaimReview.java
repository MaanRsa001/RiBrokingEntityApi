package com.maan.insurance.jpa.entity.claim;

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
@Table(name="TTRN_CLAIM_REVIEW")
public class TtrnClaimReview {

	@Id
    @Column(name="SNO")
	private Integer sNo;
	
	@Column(name="CONTRACT_NO")
	private String contractNo;
	
	@Column(name="LAYER_NO")
	private Integer layerNo;
	
	@Column(name="CLAIM_NO")
	private String claimNo;
	
	@Column(name="REVIEW_DATE")
	private Date reviewDate;
	
	@Column(name="REVIEW_DONE_BY")
	private String reviewDoneBy;
	
	@Column(name="INCEPTION_DATE")
	private Date inceptionDate;
	
	@Column(name="EXPIRY_DATE")
	private Date expiryDate;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="STATUS")
	private String status;
}
