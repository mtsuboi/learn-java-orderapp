package com.example.orderapp.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.orderapp.model.LoginUser;

public interface UserDao {
	// ユーザーを全件抽出する(ページング対応)
	public Page<LoginUser> findAll(final Pageable pageable);

	// ユーザーをuser_idで検索する
	public Optional<LoginUser> findById(final String userId);

	// ユーザーをユーザー名(部分一致)で抽出する(ページング対応)
	public Page<LoginUser> findByName(final String userName, final Pageable pageable);

	// ユーザーを追加する
	public void add(final LoginUser user);

	// ユーザーを更新する
	public int update(final LoginUser user);

	// ユーザーを削除する
	public int delete(final String userId);

}
