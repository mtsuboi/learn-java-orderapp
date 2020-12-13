package com.example.orderapp.logic;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.orderapp.model.LoginUser;
import com.example.orderapp.model.UserDetailsImpl;
import com.example.orderapp.repository.UserDao;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		// userIdが空の場合はエラー
		if(StringUtils.isEmpty(userId)) {
			throw new UsernameNotFoundException("ユーザーIDを入力してください。");
		}
		
		// ユーザーを検索する
		Optional<LoginUser> optUser = userDao.findById(userId);
		if(optUser.isPresent()) {
			LoginUser user = optUser.get();
		    return new UserDetailsImpl(user);
		} else {
			// 存在しない場合はエラー
			throw new UsernameNotFoundException("ユーザーID[" + userId + "]が存在しません。");
		}
	}

}
