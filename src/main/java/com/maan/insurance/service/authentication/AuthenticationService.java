package com.maan.insurance.service.authentication;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.authentication.AuthenticationChangesReq;
import com.maan.insurance.model.req.authentication.AuthenticationListReq;
import com.maan.insurance.model.req.authentication.GetPremiumDetailsReq;
import com.maan.insurance.model.req.premium.InsertPremiumReq;
import com.maan.insurance.model.res.authentication.AuthenticationListRes;
import com.maan.insurance.model.res.portFolio.CommonSaveRes;
import com.maan.insurance.model.res.authentication.GetPremiumDetailsRes;
import com.maan.insurance.model.res.retro.CommonResponse;

@Service
public interface AuthenticationService {

	AuthenticationListRes authenticationList(AuthenticationListReq req);

	CommonResponse authenticationChanges(AuthenticationChangesReq req);

	GetPremiumDetailsRes getPremiumDetails(GetPremiumDetailsReq req);

}
