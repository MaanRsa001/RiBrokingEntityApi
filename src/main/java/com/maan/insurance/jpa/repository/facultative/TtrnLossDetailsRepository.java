package com.maan.insurance.jpa.repository.facultative;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.facultative.TtrnLossDetails;

@Repository
public interface TtrnLossDetailsRepository extends JpaRepository<TtrnLossDetails, Integer>  {

}
