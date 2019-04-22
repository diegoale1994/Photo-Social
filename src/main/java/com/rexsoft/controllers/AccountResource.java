package com.rexsoft.controllers;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rexsoft.models.User;
import com.rexsoft.services.AccountService;

@RestController
@RequestMapping("/user")
public class AccountResource {

	private Long UserImageId;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/list")
	public ResponseEntity<?> getUsersList(){
		List<User> users = accountService.userList();
		if(users.isEmpty()) {
			return new ResponseEntity<>("No users found", HttpStatus.OK);
		}else {
			return new ResponseEntity<>(users, HttpStatus.OK);
		}
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<?> getUserInfo(@PathVariable("username") String username){
		User user = accountService.findByUsername(username);
		if(user == null) {
			return new ResponseEntity<>("No user found", HttpStatus.OK);
		}else {
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/findByUsername{username}")
	public ResponseEntity<?> getUsersListByUsername(@PathVariable("username") String username){
		List<User> users = accountService.getUserListByUsername(username);
		if(users.isEmpty()) {
			return new ResponseEntity<>("No users found", HttpStatus.OK);
		}else {
			return new ResponseEntity<>(users, HttpStatus.OK);
		}
		
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody HashMap<String, String> request){
		String username = request.get("username");
		if(accountService.findByUsername(username) != null) {
			return new ResponseEntity<>("UsernameExist!", HttpStatus.CONFLICT);
		}
		String email = request.get("email");
		if(accountService.findByEmail(email) != null) {
			return new ResponseEntity<>("EmailExist!", HttpStatus.CONFLICT);
		}
		
		String name = request.get("name");
		try {
			User user = accountService.saveUser(name, username, email);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error ocurred", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PutMapping("/update")
	public ResponseEntity<?> update(@RequestBody HashMap<String, String> request){
		String id = request.get("id");
		User user = accountService.findById(Long.parseLong(id));
		if(user == null) {
			return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
		}
		try {
			accountService.updateUser(user, request);
			UserImageId = user.getId();
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error Ocurred", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/photo/upload")
	public ResponseEntity<?> uploadPhoto(HttpServletRequest request){
		try {
			accountService.saveUserImage(request, UserImageId);
			return new ResponseEntity<>("Image has been uploaded", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error ocurred", HttpStatus.BAD_REQUEST);
		}
	}
}
