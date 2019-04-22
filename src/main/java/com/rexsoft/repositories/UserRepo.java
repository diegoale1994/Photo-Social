package com.rexsoft.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rexsoft.models.User;

public interface UserRepo extends JpaRepository<User, Long> {

	public User findByUsername(String username);
	
	@Query("SELECT user FROM User user WHERE user.id=:x")
	public User findUserById(@Param("x") Long id);
	public User findByEmail(String email);
	public List<User> findByUsernameContaining(String username);
	
}
