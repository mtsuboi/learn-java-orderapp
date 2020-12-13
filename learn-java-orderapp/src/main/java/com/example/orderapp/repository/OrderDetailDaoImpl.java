package com.example.orderapp.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.orderapp.model.OrderDetail;

@Repository
public class OrderDetailDaoImpl implements OrderDetailDao {
	
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<OrderDetail> findById(String orderId) {
		// 受注明細をorder_idで抽出する
		String sql = "SELECT order_id, order_detail_no, item_id, item_name, item_price, order_quantity, order_detail_amount FROM order_detail WHERE order_id = :orderId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderId",orderId);
		return jdbcTemplate.query(sql, param, new BeanPropertyRowMapper<OrderDetail>(OrderDetail.class));
	}

	@Override
	public void add(OrderDetail orderDetail) {
		// 受注明細を追加する
		String sql = "INSERT INTO order_detail (order_id, order_detail_no, item_id, item_name, item_price, order_quantity, order_detail_amount) VALUES (:orderId, :orderDetailNo, :itemId, :itemName, :itemPrice, :orderQuantity, :orderDetailAmount)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderDetail);
		jdbcTemplate.update(sql, param);
	}

	@Override
	public int deleteByOrderId(String orderId) {
		// 受注明細を削除する
		String sql = "DELETE FROM order_detail WHERE order_id = :orderId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderId",orderId);
		return jdbcTemplate.update(sql, param);
	}

}
