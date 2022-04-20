package com.auxime.contract.builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.model.Cape;
import com.auxime.contract.model.Rates;
import com.auxime.contract.model.enums.PortageCompanies;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ContractsSpecification {

        private static final String RATE = "rates";
        private static final String CONTRACT_TITLE = "contractTitle";
        private static final String STARTING_DATE = "startingDate";

        public Specification<Cape> getAllCape(String filter, LocalDate startDate, LocalDate endDate,
                        ContractState contractState, PortageCompanies structureContract, Integer rate) {
                return (root, query, criteriaBuilder) -> {
                        List<Predicate> predicates = new ArrayList<>();
                        Join<Cape, Rates> joinRates = root.join(RATE, JoinType.INNER);
                        if (filter != null && !filter.isEmpty()) {
                                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(CONTRACT_TITLE)),
                                "%" + filter.toLowerCase() + "%"));
                        }
                        if (contractState != null) {
                                predicates.add(criteriaBuilder.equal(root.get("contractState"), contractState));
                        }
                        if (structureContract != null) {
                                predicates.add(criteriaBuilder.equal(root.get("structureContract"), structureContract));
                        }
                        if (rate != null) {
                                predicates.add(criteriaBuilder.equal(joinRates.get("rate"), rate));
                        }
                        query.orderBy(criteriaBuilder.asc(root.get(STARTING_DATE)));
                        query.distinct(true);
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };
        }

}
