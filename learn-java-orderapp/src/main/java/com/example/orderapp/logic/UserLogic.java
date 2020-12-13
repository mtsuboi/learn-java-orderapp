package com.example.orderapp.logic;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.orderapp.form.UserForm;

public interface UserLogic {
	// ユーザーを全件抽出する(ページング対応)
	public Page<UserForm> findAll(final Pageable pageable);

	// ユーザーをuser_idで検索する
	public Optional<UserForm> findById(final String userId);

	// ユーザーをユーザー名(部分一致)で検索する(ページング対応)
	public Page<UserForm> findByName(final String userName, final Pageable pageable);

	// ユーザーを追加する
	public void add(final UserForm userForm);

	// ユーザー更新をする
	public int update(final UserForm userForm);

	// ユーザーを削除する
	public int delete(final String userId);
}
