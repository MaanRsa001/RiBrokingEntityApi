package com.maan.insurance.service.impl.upload;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.maan.insurance.model.entity.TtrnDocUploadDetails;
import com.maan.insurance.model.req.placement.SendMailReq;
import com.maan.insurance.validation.Formatters;

@Repository
public class UploadCustomRepositoryImple implements UploadCustomRepository{
	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	@Autowired
	EntityManager em;
	
	@Autowired
	private Formatters fm;
	
	@Override
	public List<Tuple> getMailAttachList(SendMailReq bean) {
		List<Tuple> list=null;
		try {
		//GET_EX_DOC_PRO_LIST
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		Root<TtrnDocUploadDetails> pm = query.from(TtrnDocUploadDetails.class);
					
		
		
		query.multiselect(pm.get("docId").alias("DOC_ID"),
				pm.get("docDesc").alias("DOC_DESCRIPTION"),pm.get("orgFileName").alias("ORG_FILE_NAME")
				,pm.get("ourFileName").alias("OUR_FILE_NAME")).distinct(true); 
		List<Predicate> predicates = new ArrayList<>();
		
		predicates.add(cb.equal(pm.get("branchCode"), bean.getBranchCode()));
		predicates.add(cb.equal(pm.get("docType"), "99999"));
		predicates.add(cb.equal(pm.get("tranNo"), bean.getEproposalNo()));
		
		query.where(predicates.toArray(new Predicate[0]));
		
		TypedQuery<Tuple> result = em.createQuery(query);
		list = result.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	}
