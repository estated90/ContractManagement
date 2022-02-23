package com.auxime.contract.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auxime.contract.model.CommercialContract;

@Repository
public interface CommercialRepository extends JpaRepository<CommercialContract, UUID> {

	/**
	 * @param accountId Id of the account to retrieve the cape from.
	 * @return An optional list of Cape
	 */
	@Query("SELECT i FROM CommercialContract i WHERE accountId= :accountId")
	List<CommercialContract> findByAccountId(@Param("accountId") UUID accountId);
	
}
