package com.auxime.contract.repository;

import java.util.List;
import java.util.UUID;

import com.auxime.contract.model.Cape;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository on the CAPE
 * @author Nicolas
 *
 */
@Repository
public interface CapeRepository extends JpaRepository<Cape, UUID>, JpaSpecificationExecutor<Cape> {

	/**
	 * @param accountId Id of the account to retrieve the cape from.
	 * @param paging 
	 * @return An optional list of Cape
	 */
	@Query("SELECT i FROM Cape i WHERE accountId= :accountId AND status=true")
	Page<Cape> findByAccountId(@Param("accountId") UUID accountId, Pageable paging);
	
	/**
	 * @param contractId Id of the contract to find the amendment linked.
	 * @param paging 
	 * @return An optional list of Cape
	 */
	@Query("SELECT i FROM Cape i WHERE contractAmendment= :contractId AND status=true")
	Page<Cape> findAllAmendment(@Param("contractId") UUID contractId, Pageable paging);
	
}
