package com.auxime.contract.repository;

import java.util.UUID;

import com.auxime.contract.model.PortageConvention;

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
public interface PortageConventionRepository extends JpaRepository<PortageConvention, UUID>, JpaSpecificationExecutor<PortageConvention> {

	/**
	 * @param accountId Id of the account to retrieve the cape from.
	 * @param paging 
	 * @return An optional list of Cape
	 */
	@Query("SELECT i FROM PortageConvention i WHERE accountId= :accountId")
	Page<PortageConvention> findByAccountId(@Param("accountId") UUID accountId, Pageable paging);
	
	/**
	 * @param contractId Id of the contract to find the amendment linked.
	 * @param paging 
	 * @return An optional list of Portage Convention
	 */
	@Query("SELECT i FROM PortageConvention i WHERE contractAmendment= :contractId AND status=true")
	Page<PortageConvention> findAllAmendment(@Param("contractId") UUID contractId, Pageable paging);
}
