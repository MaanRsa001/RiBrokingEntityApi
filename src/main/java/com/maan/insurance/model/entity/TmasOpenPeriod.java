package com.maan.insurance.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@Table(name="TMAS_OPEN_PERIOD")
public class TmasOpenPeriod implements Serializable{  
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SNO")
	private BigDecimal sno;
	
	
	@Column(name = "START_DATE")
	private Date startDate;
	

	@Column(name = "END_DATE")
	private Date endDate;
	
	@Column(name = "YEAR")
	private BigDecimal year;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "BRANCH_CODE")
	private String branchCode;

	public TmasOpenPeriod(BigDecimal sno, Date startDate, Date endDate, BigDecimal year, String status, String remarks,
			String branchCode) {
		super();
		this.sno = sno;
		this.startDate = startDate;
		this.endDate = endDate;
		this.year = year;
		this.status = status;
		this.remarks = remarks;
		this.branchCode = branchCode;
	}
	
	
}
