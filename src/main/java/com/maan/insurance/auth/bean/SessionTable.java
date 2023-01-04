package com.maan.insurance.auth.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="MARINE_SESSION_TABLE")
@Getter
@Setter
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class SessionTable {

	@Id
	@Column(name="TOKEN_ID")
	private String tokenid;
	@Column(name="LOGIN_ID")
	private String loginid;
	@Column(name="BRANCH_CODE")
	private String branchcode;
	@Column(name="BRANCH_NAME")
	private String branchname;
	@Column(name="USERTYPE")
	private String usertype;
	@Column(name="PRODUCT_ID")
	private String productid;
	@Column(name="OPENCOVER_NO")
	private String opencoverno;
	@Column(name="COUNTRY_ID")
	private String countryid;
	@Column(name="AGENCY_CODE")
	private String agencycode;
	@Column(name="TEMP_TOKENID")
	private String temptokenid;
	@Column(name="CURRENCY_NAME")
	private String currencyname;
	@Column(name="MENU_ID")
	private String menuid;
	@Column(name="ENTRY_DATE")
	private Date entrydate;
	@Column(name="API_LINK")
	private String apilink;
	@Column(name="REGION_CODE")
	private String regioncode;

}
