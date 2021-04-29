package com.example.orderapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.orderapp.logic.UserDetailsServiceImpl;

@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebSecurityBasicAuthenticationConfig extends WebSecurityConfigurerAdapter {

	@Autowired UserDetailsServiceImpl userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// orderServiceのみBasic認証
		http.antMatcher("/orderService/**")
			.authorizeRequests()
			.anyRequest().authenticated()
			.and().httpBasic()
			.and().csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// パスワードエンコーダー
		// PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		
		// インメモリでのユーザー認証を設定
		// auth.inMemoryAuthentication()
		// 	.withUser("mtsuboi").password(encoder.encode("mtsuboipass")).roles("USER");
		
		// DB認証を設定
		auth.userDetailsService(userDetailsService);
	}

}
