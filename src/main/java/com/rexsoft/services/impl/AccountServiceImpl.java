package com.rexsoft.services.impl;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rexsoft.models.Role;
import com.rexsoft.models.User;
import com.rexsoft.repositories.RoleRepo;
import com.rexsoft.repositories.UserRepo;
import com.rexsoft.services.AccountService;

import utility.EmailConstructor;

public class AccountServiceImpl implements AccountService {

	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private EmailConstructor emailConstructor;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void saveUser(User user) {
		String password = RandomStringUtils.randomAlphanumeric(10);
		String encryptedPassword = bcryptPasswordEncoder.encode(password);
		user.setPassword(encryptedPassword);
		this.userRepo.save(user);
		mailSender.send(emailConstructor.constructorNewUserEmail(user, encryptedPassword));
	}

	@Override
	public User findByUsername(String username) {
		return this.userRepo.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return this.userRepo.findByEmail(email);
	}

	@Override
	public List<User> userList() {
		return this.userRepo.findAll();
	}

	@Override
	public Role findUserRoleByName(String name) {
		return this.roleRepo.findRoleByName(name);
	}

	@Override
	public Role saveRole(Role role) {
		return this.roleRepo.save(role);
	}

	@Override
	public void updateUser(User user) {
		String password = user.getPassword();
		String encryptedPassword = bcryptPasswordEncoder.encode(password);
		user.setPassword(encryptedPassword);
		this.userRepo.save(user);
		mailSender.send(emailConstructor.constructorNewUserEmail(user, encryptedPassword));
	}

	@Override
	public User findById(Long id) {
		return this.userRepo.findById(id).orElse(null);
	}

	@Override
	public void deleteUser(User user) {
		this.userRepo.delete(user);
	}

	@Override
	public void resetPassword(User user) {
		String password = RandomStringUtils.randomAlphanumeric(10);
		String encryptedPassword = bcryptPasswordEncoder.encode(password);
		user.setPassword(encryptedPassword);
		userRepo.save(user);
		mailSender.send(emailConstructor.constructResetPasswordEmail(user, password));
	}

	@Override
	public List<User> getUserListByUsername(String username) {
		return this.userRepo.findByUsernameContaining(username);
	}

	@Override
	public User simpleSave(User user) {
		this.userRepo.save(user);
		mailSender.send(emailConstructor.constructUpdateUserProfileEmail(user));
		return user;
	}

}
