package com.maan.insurance.auth.service;

import javax.servlet.http.HttpServletRequest;

import com.maan.insurance.auth.dto.CommonLoginResponse;
import com.maan.insurance.auth.dto.LoginRequest;

public interface AuthendicationService {

	CommonLoginResponse checkUserLogin(LoginRequest mslogin, HttpServletRequest http);



}
