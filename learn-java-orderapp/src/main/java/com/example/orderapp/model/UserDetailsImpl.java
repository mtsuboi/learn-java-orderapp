package com.example.orderapp.model;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private LoginUser user;
	private Collection<GrantedAuthority> authorities;
	
	public UserDetailsImpl() {}

	public UserDetailsImpl(LoginUser user) {
		this.user = user;
		this.authorities = new ArrayList<>();
		this.authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
	}

	public String getUserId() {
		return user.getUserId();
	}
	
	public String getUserViewName() {
		return user.getUserName();
	}
	
	public String getRole() {
		return user.getRole();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(user.getPassword());
	}

	@Override
	public String getUsername() {
		return user.getUserId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
