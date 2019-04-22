package com.rexsoft.services;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.rexsoft.models.Role;
import com.rexsoft.models.User;

public interface AccountService {
	
	public User saveUser(String name, String username, String email);
	public User findByUsername(String username);
	public User findByEmail(String email);
	public List<User> userList();
	public Role findUserRoleByName(String role);
	public Role saveRole(Role role);
	public User findById(Long id);
	public void deleteUser(User user);
	public void resetPassword(User user);
	public List<User> getUserListByUsername(String username);
	public User simpleSave(User user); 
	public String saveUserImage(HttpServletRequest request, Long userImageId);
	public void updateUserPassword(User user, String newpasword);
	public List<User> getUsersListByUsername(String name);
	public User updateUser(User user, HashMap<String, String> request);
}
