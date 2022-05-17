package com.auxime.contract.repository;

import java.util.UUID;

import com.auxime.contract.model.PermanentContract;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Nicolas
 *
 */
@Repository
public interface PermanentContractRepository extends JpaRepository<PermanentContract, UUID>, JpaSpecificationExecutor<PermanentContract> {

	/**
	 * @param accountId Id of the account to retrieve the cape from.
	 * @param paging 
	 * @return An optional list of Cape
	 */
	@Query("SELECT i FROM PermanentContract i WHERE accountId= :accountId")
	Page<PermanentContract> findByAccountId(@Param("accountId") UUID accountId, Pageable paging);
	
	/**
	 * @param contractId Id of the contract to find the amendment linked.
	 * @param paging 
	 * @return An optional list of Permanent Contract
	 */
	@Query("SELECT i FROM PermanentContract i WHERE contractAmendment= :contractId AND status=true")
	Page<PermanentContract> findAllAmendment(@Param("contractId") UUID contractId, Pageable paging);
}
