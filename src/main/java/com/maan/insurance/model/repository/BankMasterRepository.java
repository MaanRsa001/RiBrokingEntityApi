package com.maan.insurance.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.jpa.entity.treasury.BankMaster;
import com.maan.insurance.jpa.keys.BankMasterKey;

public interface BankMasterRepository  extends JpaRepository<BankMaster,BankMasterKey > , JpaSpecificationExecutor<BankMaster> {


	List<BankMaster> findByBranchCodeAndStatusOrderByBankName(String branchCode, String string);

}
