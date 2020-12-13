package com.example.orderapp.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.orderapp.constants.Role;
import com.example.orderapp.form.UserForm;
import com.example.orderapp.model.LoginUser;
import com.example.orderapp.repository.UserDao;

@Service
public class UserLogicImpl implements UserLogic {
	@Autowired
	private UserDao userDao;

	@Override
	public Page<UserForm> findAll(Pageable pageable) {
		// ユーザーを全件抽出する(ページング対応)
		List<UserForm> list = new ArrayList<UserForm>();
		Page<LoginUser> page = userDao.findAll(pageable);
		page.getContent().forEach(user -> list.add(makeUserForm(user)));
		return new PageImpl<UserForm>(list, pageable, page.getTotalElements());
	}

	@Override
	public Optional<UserForm> findById(String userId) {
		// ユーザーをuser_idで検索する
		return userDao.findById(userId).map(user -> makeUserForm(user));
	}
	
	@Override
	public Page<UserForm> findByName(String userName, Pageable pageable) {
		// ユーザーをユーザー名(部分一致)で検索する(ページング対応)
		List<UserForm> list = new ArrayList<UserForm>();
		Page<LoginUser> page = userDao.findByName(userName, pageable);
		page.getContent().forEach(user -> list.add(makeUserForm(user)));
		return new PageImpl<UserForm>(list, pageable, page.getTotalElements());
	}

	@Override
	@Transactional
	public void add(UserForm userForm) {
		// ユーザーを追加する
		userDao.add(makeUser(userForm));
	}

	@Override
	@Transactional
	public int update(UserForm userForm) {
		// ユーザーを更新する
		return userDao.update(makeUser(userForm));
	}
	
	@Override
	@Transactional
	public int delete(String userId) {
		// ユーザーを削除する
		return userDao.delete(userId);
	}

	private LoginUser makeUser(UserForm userForm) {
		//UserFormのデータをUserに入れて返す
		LoginUser user = new LoginUser();
		user.setUserId(userForm.getUserId());
		user.setUserName(userForm.getUserName());
		user.setPassword(userForm.getPassword());
		user.setRole(userForm.getRole().name());
		
		return user;
	}
	
	private UserForm makeUserForm(LoginUser user) {
		//UserのデータをUserFormに入れて返す
		UserForm userForm = new UserForm();
		userForm.setUserId(user.getUserId());
		userForm.setUserName(user.getUserName());
		userForm.setPassword(user.getPassword());
		userForm.setRole(Role.valueOf(user.getRole()));
		userForm.setNewUser(false);
		
		return userForm;
	}

}
