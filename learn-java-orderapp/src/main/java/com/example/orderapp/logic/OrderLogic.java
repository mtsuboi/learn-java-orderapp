package com.example.orderapp.logic;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.orderapp.constants.OrderStatus;
import com.example.orderapp.form.OrderForm;

public interface OrderLogic {
	// 受注をステータス指定で抽出する
	public List<OrderForm> findByStatus(final OrderStatus orderStatus);

	// 受注をステータス指定で抽出する(明細読み込み対応)
	public List<OrderForm> findByStatus(final OrderStatus orderStatus, final boolean isDetailRequired);

	// 受注をステータス指定で抽出する(ページング対応)
	public Page<OrderForm> findByStatus(final OrderStatus orderStatus, final Pageable pageable);

	// 受注をOrder_idで検索する
	public Optional<OrderForm> findById(final String orderId);

	// 受注を追加する
	public void add(final OrderForm orderForm);

	// 受注を更新する
	public int update(final OrderForm orderForm);

	// 受注ステータスを変更する
	public int changeStatus(final String orderId, final OrderStatus orderStatus, final LocalDate shipDate);

	// 受注を削除する
	public int delete(final String orderId);
}
