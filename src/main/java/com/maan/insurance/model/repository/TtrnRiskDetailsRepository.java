/*
 * Java domain class for entity "TtrnRiskDetails" 
 * Created on 2022-09-15 ( Date ISO 2022-09-15 - Time 15:44:15 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */
 
 /*
 * Created on 2022-09-15 ( 15:44:15 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */


package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskDetailsId;
/**
 * <h2>TtrnRiskDetailsRepository</h2>
 *
 * createdAt : 2022-09-15 - Time 15:44:15
 * <p>
 * Description: "TtrnRiskDetails" Repository
 */
 
 
 
public interface TtrnRiskDetailsRepository  extends JpaRepository<TtrnRiskDetails,TtrnRiskDetailsId > , JpaSpecificationExecutor<TtrnRiskDetails> {

	String countRsEndorsementTypeFindByRskProposalNumberAndRskContractNoAndRsEndorsementType(String proposalId,
			String proposalNo, String string);

	List<TtrnRiskDetails> findByRskProposalNumber(String proposalNo);
	


	List<TtrnRiskDetails> getMaxRskEndorsementNofindByRskProposalNumber(String proposalNo);

	List<TtrnRiskDetails> findRskStatusByRskProposalNumber(String proposalNo);

	TtrnRiskDetails findByRskProposalNumberAndRskEndorsementNo(String proposalNo, BigDecimal bigDecimal);

	TtrnRiskDetails findByRskProposalNumberAndRskEndorsementNo(String proposalNo, String endorsmentNo);

	TtrnRiskDetails findByRskProposalNumberAndRskEndorsementNoAndRskLayerNo(String proposalNo, BigDecimal bigDecimal,
			BigDecimal bigDecimal2);

	TtrnRiskDetails findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(String proposalNo);

	List<TtrnRiskDetails> findByRskContractNo(String proposalNo);

}
