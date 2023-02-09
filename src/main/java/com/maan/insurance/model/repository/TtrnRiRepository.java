package com.maan.insurance.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnRi;
import com.maan.insurance.model.entity.TtrnRiId;

public interface TtrnRiRepository extends JpaRepository<TtrnRi,TtrnRiId > , JpaSpecificationExecutor<TtrnRi> {

	List<TtrnRi> findAllOrderByRiNoDesc();

}
