package com.auxime.contract.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auxime.contract.model.Cape;

@Repository
public interface CapeRepository extends JpaRepository<Cape, UUID> {

	/**
	 * @param accountId Id of the account to retrieve the cape from.
	 * @return An optional list of Cape
	 */
	@Query("SELECT i FROM Cape i WHERE accountId= :accountId AND status=true")
	List<Cape> findByAccountId(@Param("accountId") UUID accountId);
	
	/**
	 * @param accountId Id of the account to retrieve the cape from.
	 * @return An optional list of Cape
	 */
	@Override
	@Query("SELECT i FROM Cape i WHERE status=true")
	List<Cape> findAll();
	
}
