package com.example.orderapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.example.orderapp.model.Item;

@Repository
public class ItemDaoImpl implements ItemDao {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Override
	public List<Item> findAll() {
		// 商品を全件抽出する
		String sql = "SELECT item_id, item_name, item_price FROM items";
		return jdbcTemplate.query(sql,  new BeanPropertyRowMapper<Item>(Item.class));
	}

	@Override
	public Optional<Item> findById(String itemId) {
		// 商品をitem_idで検索する
		String sql = "SELECT item_id, item_name, item_price FROM items WHERE item_id = :itemId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", itemId);
		try {
			return Optional.of(jdbcTemplate.queryForObject(sql, param,  new BeanPropertyRowMapper<Item>(Item.class)));
		} catch(EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Item> findByName(String itemName) {
		// 商品を商品名(部分一致)で抽出する
		String sql = "SELECT item_id, item_name, item_price FROM items";
		
		// 商品名の指定ありの場合だけ抽出条件を付与（指定が無ければ全件抽出）
		if(StringUtils.isEmpty(itemName)) {
			return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Item>(Item.class));
		} else {
			sql = sql + " WHERE item_name LIKE :itemName";
			SqlParameterSource param = new MapSqlParameterSource().addValue("itemName", "%" + itemName + "%");
			return jdbcTemplate.query(sql, param, new BeanPropertyRowMapper<Item>(Item.class));
		}
	}

	@Override
	public void add(Item item) {
		// 商品を追加する
		String sql = "INSERT INTO items (item_id, item_name, item_price) VALUES (:itemId, :itemName, :itemPrice)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		jdbcTemplate.update(sql, param);
	}

	@Override
	public int update(Item item) {
		// 商品を更新する
		String sql = "UPDATE items SET item_name = :itemName, item_price = :itemPrice WHERE item_id = :itemId";
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		return jdbcTemplate.update(sql, param);
	}

	@Override
	public int delete(String itemId) {
		// 商品を削除する
		String sql = "DELETE FROM items WHERE item_id = :itemId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", itemId);
		return jdbcTemplate.update(sql, param);
	}
	
}
