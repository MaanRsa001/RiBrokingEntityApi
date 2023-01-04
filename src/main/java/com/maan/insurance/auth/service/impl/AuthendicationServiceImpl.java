package com.maan.insurance.auth.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.auth.bean.MarinLoginModel;
import com.maan.insurance.auth.bean.SessionTable;
import com.maan.insurance.auth.dto.ClaimLoginResponse;
import com.maan.insurance.auth.dto.CommonLoginResponse;
import com.maan.insurance.auth.dto.LoginRequest;
import com.maan.insurance.auth.repository.LoginMasterRepository;
import com.maan.insurance.auth.repository.SessionDetailsRepository;
import com.maan.insurance.auth.service.AuthendicationService;
import com.maan.insurance.auth.token.JwtTokenUtil;
import com.maan.insurance.auth.token.passwordEnc;

@Service
public class AuthendicationServiceImpl implements AuthendicationService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private LoginMasterRepository loginRepo;
	@Autowired
	private SessionDetailsRepository sessionRep;
	
	private Logger log = LogManager.getLogger(AuthendicationServiceImpl.class);
	
	@Override
	public CommonLoginResponse checkUserLogin(LoginRequest mslogin, HttpServletRequest http) {
		CommonLoginResponse res = new CommonLoginResponse();
		ClaimLoginResponse response = new ClaimLoginResponse();
		try {
			passwordEnc passEnc = new passwordEnc();
			String epass = passEnc.crypt(mslogin.getPassword().trim());
			log.info("Encrpted password "+epass);
			List<MarinLoginModel> login =loginRepo.findByLoginidLoginidAndLoginidPassword(mslogin.getUserId(),epass);
			if (!CollectionUtils.isEmpty(login)) {
				http.getSession().removeAttribute(mslogin.getUserId());
				String token = jwtTokenUtil.doGenerateToken(mslogin.getUserId());
				log.info("-----token------" + token);
				SessionTable session = new SessionTable();
				session.setLoginid(mslogin.getUserId());
				session.setTokenid(token);
				//session.setStatus("Y");
				String temptoken = bCryptPasswordEncoder.encode("getLogincheck");
				session.setTemptokenid(temptoken);
				session.setEntrydate(new Date());
				session =sessionRep.save(session);
				response= setTokenResponse(session,login);
				res.setLoginResponse(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	private ClaimLoginResponse setTokenResponse(SessionTable session, List<MarinLoginModel> login) {
		ClaimLoginResponse r = new ClaimLoginResponse();
		try {
			MarinLoginModel log =login.get(0);
			r.setToken(session.getTemptokenid());
			r.setLoginId(log.getLoginid().getLoginid());
			r.setUserName(log.getUsername());
			r.setEmail(StringUtils.isBlank(log.getUsermail())?"":log.getUsermail());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return r;
		
	}
	
}
