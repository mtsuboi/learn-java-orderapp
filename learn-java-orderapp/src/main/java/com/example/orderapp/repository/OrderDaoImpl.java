package com.example.orderapp.repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.orderapp.constants.OrderStatus;
import com.example.orderapp.model.Order;

@Repository
public class OrderDaoImpl implements OrderDao {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<Order> findByStatus(OrderStatus orderStatus) {
		// 受注をステータス指定で抽出する
		String sql = "SELECT order_id, order_status, order_date, ship_date, customer_name, customer_zipcode, customer_address, customer_tel, order_amount FROM orders WHERE order_status = :orderStatus ORDER BY order_id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderStatus",orderStatus.getCode());
		return jdbcTemplate.query(sql, param, new OrderRowMapper());
	}

	@Override
	public Page<Order> findByStatus(OrderStatus orderStatus, Pageable pageable) {
		// 受注をステータス指定で抽出する(ページング対応)
		String sqlCount = "SELECT count(*) FROM orders WHERE order_status = :orderStatus";
		String sql = "SELECT order_id, order_status, order_date, ship_date, customer_name, customer_zipcode, customer_address, customer_tel, order_amount FROM orders WHERE order_status = :orderStatus ORDER BY order_id LIMIT :limit OFFSET :offset";
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("orderStatus",orderStatus.getCode())
				.addValue("limit", pageable.getPageSize()).addValue("offset", pageable.getOffset());

		int count = jdbcTemplate.queryForObject(sqlCount, param, Integer.class);
		List<Order> list = jdbcTemplate.query(sql, param, new OrderRowMapper());
		return new PageImpl<Order>(list, pageable, count);
	}

	@Override
	public Optional<Order> findById(String orderId) {
		// 受注をOrder_idで検索する
		String sql = "SELECT order_id, order_status, order_date, ship_date, customer_name, customer_zipcode, customer_address, customer_tel, order_amount FROM orders WHERE order_id = :orderId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderId",orderId);
		try {
			return Optional.of(jdbcTemplate.queryForObject(sql, param, new OrderRowMapper()));
		} catch(EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public String numberingOrderId() {
		// Order_idを採番する
		String sql = "SELECT MAX(order_id) AS order_id from orders";
		SqlParameterSource param = new MapSqlParameterSource();
		String maxOrderId = jdbcTemplate.queryForObject(sql, param, String.class);
		if(maxOrderId == null) {
			return "000001";
		} else {
			return String.format("%06d", Integer.parseInt(maxOrderId) + 1);
		}
	}

	@Override
	public void add(Order order) {
		// 受注を追加する
		String sql = "INSERT INTO orders (order_id, order_status, order_date, ship_date, customer_name, customer_zipcode, customer_address, customer_tel, order_amount) VALUES (:orderId, :orderStatus, :orderDate, :shipDate, :customerName, :customerZipcode, :customerAddress, :customerTel, :orderAmount)";
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("orderId", order.getOrderId())
				.addValue("orderStatus", order.getOrderStatus().getCode())
				.addValue("orderDate",order.getOrderDate())
				.addValue("shipDate",order.getShipDate())
				.addValue("customerName",order.getCustomerName())
				.addValue("customerZipcode", order.getCustomerZipcode())
				.addValue("customerAddress", order.getCustomerAddress())
				.addValue("customerTel", order.getCustomerTel())
				.addValue("orderAmount", order.getOrderAmount());
		jdbcTemplate.update(sql, param);
	}

	@Override
	public int update(Order order) {
		// 受注を更新する
		String sql = "UPDATE orders SET order_date = :orderDate, ship_date = :shipDate, customer_name = :customerName, customer_zipcode = :customerZipcode, customer_address = :customerAddress, customer_tel = :customerTel, order_amount = :orderAmount WHERE order_id = :orderId";
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("orderDate",order.getOrderDate())
				.addValue("shipDate",order.getShipDate())
				.addValue("customerName",order.getCustomerName())
				.addValue("customerZipcode", order.getCustomerZipcode())
				.addValue("customerAddress", order.getCustomerAddress())
				.addValue("customerTel", order.getCustomerTel())
				.addValue("orderAmount", order.getOrderAmount())
				.addValue("orderId", order.getOrderId());
		return jdbcTemplate.update(sql, param);
	}

	@Override
	public int changeStatus(String orderId, OrderStatus orderStatus, LocalDate shipDate) {
		// 受注ステータスを変更する
		String sql = "UPDATE orders SET order_status = :orderStatus, ship_date = :shipDate WHERE order_id = :orderId";
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("orderStatus",orderStatus.getCode())
				.addValue("shipDate", shipDate)
				.addValue("orderId", orderId);
		return jdbcTemplate.update(sql, param);
	}

	@Override
	public int delete(String orderId) {
		// 受注を削除する
		String sql = "DELETE FROM orders WHERE order_id = :orderId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderId",orderId);
		return jdbcTemplate.update(sql, param);
	}

	private class OrderRowMapper implements RowMapper<Order> {
		public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
			//データベースから取得したResultSetをOrderにマッピング
			Order order = new Order();
			order.setOrderId(rs.getString("order_id"));
			order.setOrderStatus(OrderStatus.getByCode(rs.getString("order_status")));
			Optional<Date> optOrderDate = Optional.ofNullable(rs.getDate("order_date"));
			optOrderDate.ifPresent(orderDate -> order.setOrderDate(orderDate.toLocalDate()));
			Optional<Date> optShipDate = Optional.ofNullable(rs.getDate("ship_date"));
			optShipDate.ifPresent(shipDate -> order.setShipDate(shipDate.toLocalDate()));
			order.setCustomerName(rs.getString("customer_name"));
			order.setCustomerZipcode(rs.getString("customer_zipcode"));
			order.setCustomerAddress(rs.getString("customer_address"));
			order.setCustomerTel(rs.getString("customer_tel"));
			order.setOrderAmount(rs.getInt("order_amount"));
			return order;
		}
	}
}
