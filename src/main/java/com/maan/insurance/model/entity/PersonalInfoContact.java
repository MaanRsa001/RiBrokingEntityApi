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
@IdClass(PersonalInfoContactId.class)
@Table(name="PERSONAL_INFO_CONTACT")
public class PersonalInfoContact  implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="AMEND_ID", nullable=false)
	    private BigDecimal amendId ;

	    @Id
	    @Column(name="CONTACT_SNO", nullable=false)
	    private BigDecimal contactSno ;

	    @Id
	    @Column(name="CUSTOMER_ID", nullable=false)
	    private BigDecimal customerId ;

	    @Id
	    @Column(name="BRANCH_CODE", nullable=false)
	    private String     branchCode ;

	    //--- ENTITY DATA FIELDS 
	    @Column(name="DEPARTMENT")
	    private String     department ;


	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="ENTRY_DATE")
	    private Date       entryDate ;

	    @Column(name="EMAIL")
	    private String     email ;

	    @Column(name="STATUS", length=1)
	    private String     status ;

	    @Column(name="FAX_NUMBER", length=10)
	    private String     faxNumber ;

	    @Column(name="REMARKS")
	    private String     remarks ;

	    @Column(name="SUB_DEPARTMENT")
	    private String subDepartment ;

	    @Column(name="TELEPHONE")
	    private String telephone ;


	    //--- ENTITY LINKS ( RELATIONSHIP )


	}
