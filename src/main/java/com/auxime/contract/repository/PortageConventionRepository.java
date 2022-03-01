package com.auxime.contract.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auxime.contract.model.PortageConvention;

/**
 * @author Nicolas
 *
 */
@Repository
public interface PortageConventionRepository extends JpaRepository<PortageConvention, UUID> {

	/**
	 * @param accountId Id of the account to retrieve the cape from.
	 * @return An optional list of Cape
	 */
	@Query("SELECT i FROM PortageConvention i WHERE accountId= :accountId")
	List<PortageConvention> findByAccountId(@Param("accountId") UUID accountId);
	
	/**
	 * @param contractId Id of the contract to find the amendment linked.
	 * @return An optional list of Portage Convention
	 */
	@Query("SELECT i FROM PortageConvention i WHERE contractAmendment= :contractId AND status=true")
	List<PortageConvention> FindAllAmendment(@Param("contractId") UUID contractId);
}
