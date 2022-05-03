package com.auxime.contract.repository;

import java.util.UUID;

import com.auxime.contract.constants.ContractStatus;
import com.auxime.contract.model.CommercialContract;

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
public interface CommercialRepository extends JpaRepository<CommercialContract, UUID> , JpaSpecificationExecutor<CommercialContract>{

	/**
	 * @param accountId Id of the account to retrieve the cape from.
	 * @param paging 
	 * @return An optional list of Cape
	 */
	@Query("SELECT i FROM CommercialContract i WHERE accountId= :accountId")
	Page<CommercialContract> findByAccountId(@Param("accountId") UUID accountId, Pageable paging);
	
	/**
	 * @param contractId Id of the contract to find the amendment linked.
	 * @param paging 
	 * @return An optional list of Commercial Contract
	 */
	@Query("SELECT i FROM CommercialContract i WHERE contractAmendment= :contractId AND status=true")
	Page<CommercialContract> findAllAmendment(@Param("contractId") UUID contractId, Pageable paging);

	@Query(value = "SELECT count(a) FROM CommercialContract a  where validatorId = :validatorId AND a.status=:status AND a.contractStatus=:contractStatus")
	public Integer count(@Param("validatorId") UUID validatorId, @Param("status") boolean status, @Param("contractStatus") ContractStatus contractStatus);
}
