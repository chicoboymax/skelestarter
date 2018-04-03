package com.kapparhopi.skelestarter.backend.persistence.repositories;

import com.kapparhopi.skelestarter.backend.persistence.domain.backend.Plan;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mdrouin
 * @since 2018-04-03
 */
@Repository
public interface PlanRepository extends CrudRepository<Plan, Integer> {
}
