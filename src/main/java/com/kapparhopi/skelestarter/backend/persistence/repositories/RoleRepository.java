package com.kapparhopi.skelestarter.backend.persistence.repositories;

import com.kapparhopi.skelestarter.backend.persistence.domain.backend.Role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mdrouin
 * @since 2018-04-03
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
