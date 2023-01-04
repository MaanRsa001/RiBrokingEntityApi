package com.maan.insurance.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.auth.bean.MarinLoginModel;
import com.maan.insurance.auth.dto.LoginRequest;
import com.maan.insurance.auth.service.CriteriaQueryService;
import com.maan.insurance.auth.service.LoginValidatedService;
import com.maan.insurance.error.Error;

@Component
public class LoginValidatedServiceImpl implements LoginValidatedService {

	@Autowired
	private CriteriaQueryService criteriaQuery;

	private Logger log = LogManager.getLogger(LoginValidatedServiceImpl.class);

	public List<Error> loginInputValidation(LoginRequest req) {
		List<Error> list = new ArrayList<Error>();
		try {
			if (StringUtils.isBlank(req.getUserId())) {
				list.add(new Error("", "UserId", "Please enter username"));
			}
			if (StringUtils.isBlank(req.getPassword())) {
				list.add(new Error("", "UserId", "Please enter password"));
			}
			if (StringUtils.isNotBlank(req.getUserId()) && StringUtils.isNotBlank(req.getPassword())) {
				List<MarinLoginModel> data = criteriaQuery.isvalidUser(req);
				if (CollectionUtils.isEmpty(data)) {

					list.add(new Error("", "User", "Please enter valid username/password"));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}