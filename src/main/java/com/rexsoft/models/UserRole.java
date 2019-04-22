package com.rexsoft.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UserRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userRoleId;
	
	@ManyToOne
	@JoinColumn(name= "user_id")
	@JsonIgnore
	private User appUser;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Role role;

	public UserRole() {
	}

	public UserRole(long userRoleId, User appUser, Role role) {
		this.userRoleId = userRoleId;
		this.appUser = appUser;
		this.role = role;
	}

	public UserRole(User appUser, Role role) {
		this.appUser = appUser;
		this.role = role;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public User getAppUser() {
		return appUser;
	}

	public void setAppUser(User appUser) {
		this.appUser = appUser;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
