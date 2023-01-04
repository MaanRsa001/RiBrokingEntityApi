package com.maan.insurance.jpa.repository.claim;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.claim.TtrnClaimPaymentArchive;

@Repository
public interface TtrnClaimPaymentArchiveRepository
		extends JpaRepository<TtrnClaimPaymentArchive, Integer> {

}
