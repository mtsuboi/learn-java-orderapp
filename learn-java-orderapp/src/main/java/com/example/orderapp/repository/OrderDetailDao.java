package com.example.orderapp.repository;

import java.util.List;

import com.example.orderapp.model.OrderDetail;

public interface OrderDetailDao {
	// 受注明細をOrder_idで検索する
	public List<OrderDetail> findById(final String orderId);

	// 受注明細を追加する
	public void add(final OrderDetail orderDetail);

	// 受注明細をOrderIdごと削除する
	public int deleteByOrderId(final String orderId);
}
