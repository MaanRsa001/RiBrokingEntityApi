/*
*  Copyright (c) 2019. All right reserved
* Created on 2022-03-11 ( Date ISO 2022-03-11 - Time 16:18:38 )
* Generated by Telosys Tools Generator ( version 3.3.0 )
*/
package com.maan.insurance.auth.service;

import java.util.List;

import com.maan.insurance.auth.bean.LoginMaster;
import com.maan.insurance.auth.dto.LoginRequest;

/**
* <h2>SessionDetailsServiceimpl</h2>
*/
public interface CriteriaQueryService  {

	List<LoginMaster> isvalidUser(LoginRequest req);


}
