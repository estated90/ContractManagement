package com.auxime.contract.builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.constants.ContractStatus;
import com.auxime.contract.model.Cape;
import com.auxime.contract.model.CommercialContract;
import com.auxime.contract.model.PermanentContract;
import com.auxime.contract.model.PortageConvention;
import com.auxime.contract.model.Rates;
import com.auxime.contract.model.TemporaryContract;
import com.auxime.contract.model.enums.PortageCompanies;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ContractsSpecification {

        private static final String RATE = "rates";
        private static final String CONTRACT_TITLE = "contractTitle";
        private static final String STARTING_DATE = "startingDate";
        private static final String CONTRACT_DATE = "contractDate";
        private static final String START_DATE = "startDate";
        private static final String END_DATE = "endDate";

        public Specification<Cape> getAllCape(String filter, LocalDate startDate, LocalDate endDate,
                        ContractState contractState, PortageCompanies structureContract, Integer rate) {
                return (root, query, criteriaBuilder) -> {
                        Map<String, LocalDate> dates = new HashMap<>();
                        dates.put(START_DATE, startDate);
                        dates.put(END_DATE, endDate);
                        List<Predicate> predicates = commonfields(root, query, criteriaBuilder,
                                        filter, dates, contractState, structureContract);
                        Join<Cape, Rates> joinRates = root.join(RATE, JoinType.INNER);
                        if (rate != null) {
                                predicates.add(criteriaBuilder.equal(joinRates.get("rate"), rate));
                        }
                        query.orderBy(criteriaBuilder.asc(root.get(STARTING_DATE)));
                        query.distinct(true);
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };
        }

        public Specification<CommercialContract> filterSqlCommercial(String filter, LocalDate startDate,
                        LocalDate endDate,
                        ContractState contractState, PortageCompanies structureContract,
                        ContractStatus contractStatus) {
                return (root, query, criteriaBuilder) -> {
                        Map<String, LocalDate> dates = new HashMap<>();
                        dates.put(START_DATE, startDate);
                        dates.put(END_DATE, endDate);
                        List<Predicate> predicates = commonfields(root, query, criteriaBuilder,
                                        filter, dates, contractState, structureContract);
                        if (contractStatus != null) {
                                predicates.add(criteriaBuilder.equal(root.get("contractStatus"), contractStatus));
                        }
                        query.orderBy(criteriaBuilder.asc(root.get(STARTING_DATE)));
                        query.distinct(true);
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };
        }

        public Specification<PermanentContract> filterSqlPermanent(String filter, LocalDate startDate,
                        LocalDate endDate,
                        ContractState contractState, PortageCompanies structureContract) {
                return (root, query, criteriaBuilder) -> {
                        Map<String, LocalDate> dates = new HashMap<>();
                        dates.put(START_DATE, startDate);
                        dates.put(END_DATE, endDate);
                        List<Predicate> predicates = commonfields(root, query, criteriaBuilder,
                                        filter, dates, contractState, structureContract);
                        query.orderBy(criteriaBuilder.asc(root.get(STARTING_DATE)));
                        query.distinct(true);
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };
        }

        public Specification<PortageConvention> filterSqlPortage(String filter, LocalDate startDate,
                        LocalDate endDate,
                        ContractState contractState, PortageCompanies structureContract) {
                return (root, query, criteriaBuilder) -> {
                        Map<String, LocalDate> dates = new HashMap<>();
                        dates.put(START_DATE, startDate);
                        dates.put(END_DATE, endDate);
                        List<Predicate> predicates = commonfields(root, query, criteriaBuilder,
                                        filter, dates, contractState, structureContract);
                        query.orderBy(criteriaBuilder.asc(root.get(STARTING_DATE)));
                        query.distinct(true);
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };
        }

        public Specification<TemporaryContract> filterSqlTemporary(String filter, LocalDate startDate,
                        LocalDate endDate,
                        ContractState contractState, PortageCompanies structureContract) {
                return (root, query, criteriaBuilder) -> {
                        Map<String, LocalDate> dates = new HashMap<>();
                        dates.put(START_DATE, startDate);
                        dates.put(END_DATE, endDate);
                        List<Predicate> predicates = commonfields(root, query, criteriaBuilder,
                                        filter, dates, contractState, structureContract);
                        query.orderBy(criteriaBuilder.asc(root.get(STARTING_DATE)));
                        query.distinct(true);
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };
        }

        public List<Predicate> commonfields(Root<?> root, CriteriaQuery<?> query,
                        CriteriaBuilder criteriaBuilder, String filter, Map<String, LocalDate> dates,
                        ContractState contractState, PortageCompanies structureContract) {
                List<Predicate> predicates = new ArrayList<>();
                LocalDate endDate = dates.get(END_DATE);
                LocalDate startDate = dates.get(START_DATE);
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
                if (startDate != null && endDate != null) {
                        predicates.add(criteriaBuilder.between(root.get(CONTRACT_DATE), startDate, endDate));
                }
                if (startDate != null) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CONTRACT_DATE), startDate));
                }
                if (endDate != null) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(CONTRACT_DATE), endDate));
                }
                predicates.add(criteriaBuilder.equal(root.get("status"), true));
                return predicates;
        }

}
