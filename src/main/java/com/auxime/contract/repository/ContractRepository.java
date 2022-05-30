package com.auxime.contract.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.auxime.contract.model.Contract;

/**
 * Repository on the CAPE
 * @author Nicolas
 *
 */
@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID>, JpaSpecificationExecutor<Contract> {
	
}
