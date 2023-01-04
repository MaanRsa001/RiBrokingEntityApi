package com.maan.insurance.auth.service;

import java.util.List;

import com.maan.insurance.auth.dto.LoginRequest;
import com.maan.insurance.error.Error;

public interface LoginValidatedService {

	List<Error> loginInputValidation(LoginRequest mslogin);

}
