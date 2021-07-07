package com.org.lob.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import com.org.lob.project.repository.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, RevisionRepository<Customer, Long, Long>,
		JpaSpecificationExecutor<Customer> {
	//https://tech.asimio.net/2020/11/06/Preventing-N-plus-1-select-problem-using-Spring-Data-JPA-EntityGraph.html
	@EntityGraph(type = EntityGraphType.FETCH, attributePaths = { "addresses" })
	Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);

	List<Customer> findAll(Specification<Customer> spec);

	// Contains search on either firstname or lastname
	List<Customer> findAllByFirstNameContainingOrLastNameContaining(String firstName, String lastName);

	Optional<Customer> findCustomerByEmailAddress(String email);
}
