package com.rexsoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rexsoft.models.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

	public Role findRoleByName(String name);
}
