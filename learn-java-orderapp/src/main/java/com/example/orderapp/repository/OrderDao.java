package com.example.orderapp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.orderapp.constants.OrderStatus;
import com.example.orderapp.model.Order;

public interface OrderDao {
	// 受注をステータス指定で抽出する
	public List<Order> findByStatus(final OrderStatus orderStatus);

	// 受注をステータス指定で抽出する(ページング対応)
	public Page<Order> findByStatus(final OrderStatus orderStatus, final Pageable pageable);

	// 受注をOrder_idで検索する
	public Optional<Order> findById(final String orderId);

	// Order_idを採番する
	public String numberingOrderId();

	// 受注を追加する
	public void add(final Order order);

	// 受注を更新する
	public int update(final Order order);

	// 受注ステータスを変更する
	public int changeStatus(final String orderId, final OrderStatus orderStatus, final LocalDate shipDate);

	// 受注を削除する
	public int delete(final String orderId);
}
