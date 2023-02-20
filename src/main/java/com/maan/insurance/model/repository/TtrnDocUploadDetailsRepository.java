package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnDocUploadDetails;

public interface TtrnDocUploadDetailsRepository extends JpaRepository<TtrnDocUploadDetails,BigDecimal > , JpaSpecificationExecutor<TtrnDocUploadDetails> {

	TtrnDocUploadDetails findTop1ByOrderByDocIdDesc();

}