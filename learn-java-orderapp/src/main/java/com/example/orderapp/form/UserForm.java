package com.example.orderapp.form;

import javax.validation.constraints.NotBlank;

import com.example.orderapp.constants.Role;

public class UserForm {
	@NotBlank(message = "ユーザーIDは必須です。")
	private String userId;
	
	@NotBlank(message = "ユーザー名は必須です。")
	private String userName;
	
	@NotBlank(message = "パスワードは必須です。")
	private String password;
	
	private Role role;
	
	private Boolean isNewUser;

	public UserForm() {}

	public UserForm(String userId, String userName, String password, Role role, boolean isNewUser) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.role = role;
		this.isNewUser = isNewUser;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Boolean isNewUser() {
		return isNewUser;
	}

	public void setNewUser(Boolean isNewUser) {
		this.isNewUser = isNewUser;
	}
}
