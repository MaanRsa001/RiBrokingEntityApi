package com.maan.insurance.auth.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="LOGIN_MASTER")
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MarinLoginModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private LoginMasterID loginid;
	@Column(name="AGENCY_CODE")
	private String agencycode;
	@Column(name="USERTYPE")
	private String usertype;
	@Column(name="BRANCH_CODE")
	private String branchcode;
	@Column(name="COUNTRY_ID")
	private Long countryid;
	@Column(name="LPASS5")
	private String lpass5;
	@Column(name="LPASS4")
	private String lpass4;
	@Column(name="LPASS3")
	private String lpass3;
	@Column(name="LPASS2")
	private String lpass2;
	@Column(name="LPASS1")
	private String lpass1;
	@Column(name="OA_CODE")
	private String oacode;
	@Column(name="PWD_COUNT")
	private String pwdcount;
	@Column(name="PRODUCT_ID")
	private String productid;
	@Column(name="APP_ID")
	private String appid;
	@Column(name="STATUS")
	private String status;
	
	@Column(name="USERNAME")
    private String username;
	@Column(name="USERID")
	private Long userid;
	@Column(name="ACCESSTYPE")
	private String accesstype;
	@Column(name="PASSDATE")
	private Date passdate;
	@Column(name="ENTRY_DATE")
	private Date entrydate;
	@Column(name="INCEPTION_DATE")
	private Date inceptiondate;
	@Column(name="COMPANY_ID")
	private Long companyid;
	@Column(name="CREATED_BY")
	private String create_by;
	@Column(name="USER_ID_CREATION")
	private String useridcreation;
	@Column(name="AC_EXECUTIVE_CREATION")
	private String acexecutivecreation;
	@Column(name="CORE_LOGIN_ID")
	private String coreloginid;
	@Column(name="USER_MAIL")
	private String usermail;
	@Column(name="SUB_BRANCH")
	private String subbranch;
	@Column(name="ATTACHED_BRANCH")
	private String attachedbranch;
	@Column(name="MENU_ID")
	private String menuid;
	@Column(name="BROKER_CODES")
	private String brokercodes;
	@Column(name="ATTACHED_UW")
	private String attacheduw;
	@Column(name="REFERAL")
	private String referal;
	@Column(name="TOKEN_PASSWORD")
	private String tokenpassword;
	@Column(name="ENC_PASSWORD")
	private String encpassword;
	@Column(name="REGION_CODE")
	private String regioncode;
	
	@Transient
	private String loginType;
	@Transient
	private List<Error> errors;
	
	@Transient
	private String passwordMsg;
	@Transient
	private String changestatus;
}
