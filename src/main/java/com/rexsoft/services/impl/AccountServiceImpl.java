package com.rexsoft.services.impl;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rexsoft.models.Role;
import com.rexsoft.models.User;
import com.rexsoft.models.UserRole;
import com.rexsoft.repositories.RoleRepo;
import com.rexsoft.repositories.UserRepo;
import com.rexsoft.services.AccountService;
import com.rexsoft.utility.Constants;
import com.rexsoft.utility.EmailConstructor;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountService accountService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private EmailConstructor emailConstructor;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	@Transactional
	public User saveUser(String name, String username, String email) {
		String password = RandomStringUtils.randomAlphanumeric(10);
		String encryptedPassword = bCryptPasswordEncoder.encode(password);
		User appUser = new User();
		appUser.setPassword(encryptedPassword);
		appUser.setName(name);
		appUser.setUsername(username);
		appUser.setEmail(email);
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(appUser, accountService.findUserRoleByName("USER")));
		appUser.setUserRoles(userRoles);
		userRepo.save(appUser);
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(Constants.TEMP_USER.toPath());
			String fileName = appUser.getId() + ".png";
			Path path = Paths.get(Constants.USER_FOLDER + fileName);
			Files.write(path, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mailSender.send(emailConstructor.constructNewUserEmail(appUser, password));
		return appUser;
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
	public User updateUser(User user, HashMap<String, String> request) {
		String name = request.get("name");
		// String username = request.get("username");
		String email = request.get("email");
		String bio = request.get("bio");
		user.setName(name);
		// appUser.setUsername(username);
		user.setEmail(email);
		user.setBio(bio);
		userRepo.save(user);
		mailSender.send(emailConstructor.constructUpdateUserProfileEmail(user));
		return user;

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
		String encryptedPassword = bCryptPasswordEncoder.encode(password);
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

	@Override
	public String saveUserImage(HttpServletRequest request, Long userImageId) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Iterator<String> it = multipartRequest.getFileNames();
		MultipartFile multipartFile = multipartRequest.getFile(it.next());
		byte[] bytes;
		try {
			Files.deleteIfExists(Paths.get(Constants.USER_FOLDER+"/"+userImageId+".png"));
			bytes = multipartFile.getBytes();
			Path path = Paths.get(Constants.POST_FOLDER + userImageId + ".png");
			Files.write(path, bytes);
			return "User picture saved";
		}catch (Exception e) {
			return "Error ocurre, Photo Not saved";
		}
	}

	@Override
	public void updateUserPassword(User user, String newpasword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> getUsersListByUsername(String name) {
		return userRepo.findByUsernameContaining(name);
	}

}
