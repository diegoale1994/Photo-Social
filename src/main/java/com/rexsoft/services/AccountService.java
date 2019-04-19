package com.rexsoft.services;

import java.util.List;

import com.rexsoft.models.Role;
import com.rexsoft.models.User;

public interface AccountService {
	
	public void saveUser(User user);
	public User findByUsername(String username);
	public User findByEmail(String email);
	public List<User> userList();
	public Role findUserRoleByName(String role);
	public Role saveRole(Role role);
	public void updateUser(User user);
	public User findById(Long id);
	public void deleteUser(User user);
	public void resetPassword(User user);
	public List<User> getUserListByUsername(String username);
	public void simpleSave(User user); 
	
}