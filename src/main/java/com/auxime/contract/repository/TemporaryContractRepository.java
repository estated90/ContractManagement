package com.auxime.contract.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auxime.contract.model.TemporaryContract;

/**
 * @author Nicolas
 *
 */
@Repository
public interface TemporaryContractRepository extends JpaRepository<TemporaryContract, UUID> {

	/**
	 * @param accountId Id of the account to retrieve the cape from.
	 * @return An optional list of Cape
	 */
	@Query("SELECT i FROM TemporaryContract i WHERE accountId= :accountId")
	Page<TemporaryContract> findByAccountId(@Param("accountId") UUID accountId, Pageable paging);
	
	/**
	 * @param contractId Id of the contract to find the amendment linked.
	 * @param paging 
	 * @return An optional list of Temporary Contract
	 */
	@Query("SELECT i FROM TemporaryContract i WHERE contractAmendment= :contractId AND status=true")
	Page<TemporaryContract> FindAllAmendment(@Param("contractId") UUID contractId, Pageable paging);
	
}
