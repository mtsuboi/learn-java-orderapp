package com.example.orderapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.example.orderapp.model.LoginUser;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public Page<LoginUser> findAll(Pageable pageable) {
		// ユーザーを全件抽出する(ページング対応)
		String sqlCount = "SELECT count(*) FROM users";
		String sql = "SELECT user_id,  user_name, password, role FROM users LIMIT :limit OFFSET :offset";
		SqlParameterSource param = new MapSqlParameterSource().addValue("limit", pageable.getPageSize()).addValue("offset", pageable.getOffset());

		int count = jdbcTemplate.queryForObject(sqlCount, param, Integer.class);
		List<LoginUser> list = jdbcTemplate.query(sql, param, new BeanPropertyRowMapper<LoginUser>(LoginUser.class));
		return new PageImpl<LoginUser>(list, pageable, count);
	}

	@Override
	public Optional<LoginUser> findById(String userId) {
		// ユーザーをuser_idで検索する
		String sql = "SELECT user_id, user_name, password, role FROM users WHERE user_id = :userId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		try {
			return Optional.of(jdbcTemplate.queryForObject(sql, param,  new BeanPropertyRowMapper<LoginUser>(LoginUser.class)));
		} catch(EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public Page<LoginUser> findByName(String userName, Pageable pageable) {
		// ユーザーをユーザー名(部分一致)で抽出する(ページング対応)
		String sqlCount = "SELECT count(*) FROM users";
		String sql = "SELECT user_id, user_name, password, role FROM users";
		
		// ユーザー名の指定ありの場合だけ抽出条件を付与（指定が無ければ全件抽出）
		String sqlWhere = "";
		if(!StringUtils.isEmpty(userName)) {
			sqlWhere = " WHERE user_name LIKE :userName";
		}

		// LIMITとOFFSET
		String sqlLimit = " LIMIT :limit OFFSET :offset";

		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("userName", "%" + userName + "%")
				.addValue("limit", pageable.getPageSize()).addValue("offset", pageable.getOffset());
		
		int count = jdbcTemplate.queryForObject(sqlCount + sqlWhere, param, Integer.class);
		List<LoginUser> list = jdbcTemplate.query(sql + sqlWhere + sqlLimit, param, new BeanPropertyRowMapper<LoginUser>(LoginUser.class));
		return new PageImpl<LoginUser>(list, pageable, count);
	}

	@Override
	public void add(LoginUser user) {
		// ユーザーを追加する
		String sql = "INSERT INTO users (user_id, user_name, password, role) VALUES (:userId, :userName, :password, :role)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		jdbcTemplate.update(sql, param);
	}

	@Override
	public int update(LoginUser user) {
		// ユーザーを更新する
		String sql = "UPDATE users SET user_name = :userName, password = :password, role = :role WHERE user_id = :userId";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		return jdbcTemplate.update(sql, param);
	}

	@Override
	public int delete(String userId) {
		// ユーザーを削除する
		String sql = "DELETE FROM users WHERE user_id = :userId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		return jdbcTemplate.update(sql, param);
	}

}
