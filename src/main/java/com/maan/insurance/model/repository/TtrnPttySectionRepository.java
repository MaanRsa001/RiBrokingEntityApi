package com.maan.insurance.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.maan.insurance.model.entity.TtrnPttySection;
import com.maan.insurance.model.entity.TtrnPttySectionId;
@Repository
public interface TtrnPttySectionRepository extends JpaRepository<TtrnPttySection,TtrnPttySectionId> , JpaSpecificationExecutor<TtrnPttySection> {

}
