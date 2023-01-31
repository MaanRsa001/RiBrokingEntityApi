/*
 * Java domain class for entity "TtrnRiskProposal" 
 * Created on 2022-09-15 ( Date ISO 2022-09-15 - Time 15:44:16 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */
 
 /*
 * Created on 2022-09-15 ( 15:44:16 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */


package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.entity.TtrnRiskProposalId;
/**
 * <h2>TtrnRiskProposalRepository</h2>
 *
 * createdAt : 2022-09-15 - Time 15:44:16
 * <p>
 * Description: "TtrnRiskProposal" Repository
 */
 
 
 
public interface TtrnRiskProposalRepository  extends JpaRepository<TtrnRiskProposal,TtrnRiskProposalId > , JpaSpecificationExecutor<TtrnRiskProposal> {

	List<TtrnRiskProposal> getMaxRskEndorsementNoFindByRskProposalNumber(String proposalNo);

	TtrnRiskProposal findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(String proposalNo);

	TtrnRiskProposal findByRskProposalNumberAndRskEndorsementNo(String proposalNo, BigDecimal bigDecimal);

	TtrnRiskProposal findByRskEndorsementNoAndRskProposalNumber(BigDecimal bigDecimal, String string);

}
