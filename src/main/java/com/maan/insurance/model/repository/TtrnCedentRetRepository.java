package com.maan.insurance.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnCedentRet;

public interface TtrnCedentRetRepository extends JpaRepository<TtrnCedentRet,String > , JpaSpecificationExecutor<TtrnCedentRet> {

	TtrnCedentRet findTop1ByProposalNoOrderByAmendIdDesc(String proposalNo);

}
