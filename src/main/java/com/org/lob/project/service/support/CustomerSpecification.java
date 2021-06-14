package com.org.lob.project.service.support;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.org.lob.project.repository.entity.Customer;
import com.org.lob.project.service.model.CustomerSearchRequest;

public class CustomerSpecification implements Specification<Customer> {

	private static final long serialVersionUID = 1L;

	private static final String ATTR_LAST_NAME = "lastName";
	private static final String ATTR_FIRST_NAME = "firstName";
	private static final String ATTR_EMAIL_ADDRESS = "emailAddress";

	private final CustomerSearchRequest request;

	public CustomerSpecification(CustomerSearchRequest request) {
		this.request = request;
	}

	/**
	 * TODO: Prefer using Query DSL (https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/) so that we don't have to hard code attribute name
	 */
	@Override
	public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();

		if (request.isFirstNameValid()) {
            predicates.add(criteriaBuilder.equal(root.get(ATTR_FIRST_NAME), request.getFirstName()));
        }

		/**
		if (request.isFirstNameValid()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(ATTR_FIRST_NAME)),
                    "%" + request.getFirstName().toLowerCase() + "%"));
        } **/

		if (request.isLastNameValid()) {
            predicates.add(criteriaBuilder.equal(root.get(ATTR_LAST_NAME), request.getLastName()));
        }

		if (request.isEmailValid()) {
            predicates.add(criteriaBuilder.equal(root.get(ATTR_EMAIL_ADDRESS), request.getEmailAddress()));
        }

        query.orderBy(criteriaBuilder.desc(root.get(ATTR_FIRST_NAME)));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}

}
