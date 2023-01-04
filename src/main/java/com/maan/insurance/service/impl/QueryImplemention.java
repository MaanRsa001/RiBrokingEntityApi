package com.maan.insurance.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.buf.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Component
public class QueryImplemention {
	private Logger log=LogManager.getLogger(QueryImplemention.class);
	private Properties prop = new Properties();
	private Query query=null;
	@PersistenceContext
	private EntityManager em;
	
	  public QueryImplemention() {
	        try {
	        	InputStream  inputStream = getClass().getClassLoader().getResourceAsStream("OracleQuery.properties");
	        	if (inputStream != null) {
					prop.load(inputStream);
				}
	        	
	        } catch (Exception e) {
	            log.info(e);
	        }
	    }
	@Transactional
	public int updateQuery(String props, String[] arg) {
		String qutext = prop.getProperty(props);
		log.info("Inside updateQuery => " + qutext +" args=>"+ StringUtils.join(arg));
		query=setQueryProp(qutext, arg);
		int rowsUpdated =query.executeUpdate();
		return rowsUpdated;
	}
	@Transactional
	public void updateQueryWP(String props) {
		String qutext = prop.getProperty(props);
		query = em.createNativeQuery(qutext);
		log.info("Inside updateQuery => " + qutext );
		query.executeUpdate();
	}
	@Transactional
	public List<Map<String, Object>> selectList(String props, String[] arg) {
		List<Map<String,Object>> list = new ArrayList<>();
		String qutext = prop.getProperty(props);
		log.info("Inside updateQuery => " + qutext +" args=>"+ StringUtils.join(arg));
		query=setQueryProp(qutext, arg);
		query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		try {
		list = query.getResultList();
		} catch(Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	@Transactional
	public List<Map<String, Object>> selectSingle(String props, String[] arg) {
		List<Map<String,Object>> list = new ArrayList<>();
		log.info("Inside updateQuery => " + props +" args=>"+ arg);
		query=setQueryProp(props, arg);
		query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		try {
		list = query.getResultList();
		} catch(Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	public Query setQueryProp(String qutext, String[] args) {
		query = em.createNativeQuery(qutext);
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
			
				query.setParameter(i + 1, args[i]);
				
				
			}
		}
		return query;
	}

	

	public String getSequenceNo(String type,String productID,String departmentId,String branchCode, String proposalNo, String trDate) {
		String sequenceNo="";
		try {
		String qutext = prop.getProperty("GET_SEQUENCE");
		query = em.createNativeQuery(qutext);
		query.setParameter(1, type);
		query.setParameter(2, productID);
		query.setParameter(3, departmentId);
		query.setParameter(4, branchCode);
		query.setParameter(5, proposalNo);
		query.setParameter(6, trDate);
		query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<Map<String,Object>> list = query.getResultList();
		if(!CollectionUtils.isEmpty(list)) {
			sequenceNo = list.get(0).get("SEQUENCE_NO")==null?"":list.get(0).get("SEQUENCE_NO").toString();
		}
		}catch(Exception e) {
			log.info(e);  
		}
		return sequenceNo;  
	}


}
