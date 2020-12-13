package com.example.orderapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.example.orderapp.logic.UserDetailsServiceImpl;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired UserDetailsServiceImpl userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// ユーザー権限によるアクセス制御の設定(商品マスタは管理者のみアクセス可能）
		http.authorizeRequests()
			.antMatchers("/h2-console/**").permitAll() 
			.antMatchers("/item").hasRole("ADMIN")
			.antMatchers("/user").hasRole("ADMIN")
			.anyRequest().authenticated()
			.and().formLogin()
			.and().httpBasic();
		
		// H2コンソール用
		http.csrf().disable().headers().frameOptions().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*
		// パスワードエンコーダー
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		
		// インメモリでのユーザー認証を設定
		auth.inMemoryAuthentication()
			.withUser("mtsuboi").password(encoder.encode("mtsuboipass")).roles("USER")
			.and()
			.withUser("mtsuboiadmin").password(encoder.encode("mtsuboiadminpass")).roles("ADMIN");
		*/
		
		// DB認証を設定
		auth.userDetailsService(userDetailsService);
	}

}
