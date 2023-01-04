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

import com.maan.insurance.auth.bean.MarinLoginModel;
import com.maan.insurance.auth.dto.LoginRequest;
import com.maan.insurance.auth.service.CriteriaQueryService;
import com.maan.insurance.auth.token.passwordEnc;

@Component
public class CriteriaQueryServiceImpl implements CriteriaQueryService{
	
	@PersistenceContext
	private EntityManager em;
	
	public List<MarinLoginModel>  isvalidUser(LoginRequest req) {
		List<MarinLoginModel> data = new ArrayList<MarinLoginModel>();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<MarinLoginModel> query = cb.createQuery(MarinLoginModel.class);
			
			Root<MarinLoginModel> login = query.from(MarinLoginModel.class);
			//Root<ClaimLoginUserDetails> user = query.from(ClaimLoginUserDetails.class);

			passwordEnc passEnc = new passwordEnc();
			String password = passEnc.crypt(req.getPassword().trim());
			
			Predicate p1 = cb.equal(login.get("loginid").get("loginid"), req.getUserId());
			Predicate p3 = cb.equal(login.get("loginid").get("password"), password);
			Predicate p2 = cb.equal(login.get("status"), "Y");

			query.select(login).where(p1,p2,p3);

			TypedQuery<MarinLoginModel> result = em.createQuery(query);
			data = result.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
}



	

