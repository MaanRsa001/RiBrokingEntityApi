package com.maan.insurance.jpa.repository.facultative;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.facultative.TtrnFacSi;

@Repository
public interface TtrnFacSiRepository extends JpaRepository<TtrnFacSi, Integer>  {

}
