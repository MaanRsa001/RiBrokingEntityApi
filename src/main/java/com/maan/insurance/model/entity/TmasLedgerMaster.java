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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="TMAS_LEDGER_MASTER")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TmasLedgerMaster implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "COA_ID")
	private BigDecimal coaId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED")
	private Date dateCreated;
	

	@Column(name = "LEDGER_CODE")
	private String ledgerCode;
	
	@Column(name = "NAME_OF_ACCOUNT")
	private String nameOfAccount;
	
	@Column(name = "MAIN_GROUP")
	private String mainGroup;
	
	@Column(name = "GROUPED")
	private String grouped;
	
	@Column(name = "BRANCH_CODE")
	private String branchCode;
	
	@Column(name = "INTEGRATION_NAME")
	private String integrationName;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "REMARKS")
	private String remarks;
}
