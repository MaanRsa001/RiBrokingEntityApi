package com.maan.insurance.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.maan.insurance.auth.bean.LoginMaster;
import com.maan.insurance.auth.dto.LoginRequest;
import com.maan.insurance.auth.service.CriteriaQueryService;
import com.maan.insurance.auth.token.passwordEnc;

@Component
public class CriteriaQueryServiceImpl implements CriteriaQueryService{
	
	@PersistenceContext
	private EntityManager em;
	
	public List<LoginMaster>  isvalidUser(LoginRequest req) {
		List<LoginMaster> data = new ArrayList<LoginMaster>();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<LoginMaster> query = cb.createQuery(LoginMaster.class);
			
			Root<LoginMaster> login = query.from(LoginMaster.class);
			//Root<ClaimLoginUserDetails> user = query.from(ClaimLoginUserDetails.class);

			passwordEnc passEnc = new passwordEnc();
			String password = passEnc.crypt(req.getPassword().trim());
			
			Predicate p1 = cb.equal(login.get("loginid").get("loginid"), req.getUserId());
			Predicate p3 = cb.equal(login.get("loginid").get("password"), password);
			Predicate p2 = cb.equal(login.get("status"), "Y");

			query.select(login).where(p1,p2,p3);

			TypedQuery<LoginMaster> result = em.createQuery(query);
			data = result.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
}



	

