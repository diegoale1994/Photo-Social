package com.rexsoft.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rexsoft.models.User;

public interface UserRepo extends JpaRepository<User, Long> {

	public User findByUsername(String username);
	public User findUserById(Long id);
	public User findByEmail(String email);
	public List<User> findByUsernameContaining(String username);
	
}
