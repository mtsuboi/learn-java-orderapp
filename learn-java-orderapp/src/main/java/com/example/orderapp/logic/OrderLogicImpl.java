package com.example.orderapp.logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.orderapp.constants.OrderStatus;
import com.example.orderapp.form.OrderForm;
import com.example.orderapp.form.OrderFormDetail;
import com.example.orderapp.model.Order;
import com.example.orderapp.model.OrderDetail;
import com.example.orderapp.repository.OrderDao;
import com.example.orderapp.repository.OrderDetailDao;

@Service
public class OrderLogicImpl implements OrderLogic {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderDetailDao orderDetailDao;

	@Override
	public List<OrderForm> findByStatus(OrderStatus orderStatus) {
		// 指定されたステータスの受注を抽出する
		return this.findByStatus(orderStatus, false);
	}

	@Override
	public List<OrderForm> findByStatus(OrderStatus orderStatus, boolean isDetailRequired) {
		// 指定されたステータスの受注を抽出する(明細読み込み対応)
		List<OrderForm> list= new ArrayList<OrderForm>();
		orderDao.findByStatus(orderStatus).forEach(order ->
			list.add(makeOrderForm(order, isDetailRequired ? orderDetailDao.findById(order.getOrderId()) : new ArrayList<OrderDetail>()))
		);
		return list;
	}

	@Override
	public Page<OrderForm> findByStatus(OrderStatus orderStatus, Pageable pageable) {
		// 指定されたステータスの受注を抽出する(ページング対応)
		List<OrderForm> list= new ArrayList<OrderForm>();
		Page<Order> page = orderDao.findByStatus(orderStatus, pageable);
		page.getContent().forEach(order -> list.add(makeOrderForm(order, new ArrayList<OrderDetail>())));
		return new PageImpl<OrderForm>(list, pageable, page.getTotalElements());
	}

	@Override
	public Optional<OrderForm> findById(String orderId) {
		// 受注をOrder_idで検索する
		return orderDao.findById(orderId).map(order -> makeOrderForm(order, orderDetailDao.findById(orderId)));
	}

	@Override
	@Transactional
	public void add(OrderForm orderForm) {
		// 受注IDを採番
		orderForm.setOrderId(orderDao.numberingOrderId());
		// 受注を追加する
		orderDao.add(makeOrder(orderForm));
		orderForm.getOrderDetailList().forEach(orderDetailForm -> orderDetailDao.add(makeOrderDetail(orderForm.getOrderId(), orderDetailForm)));
	}

	@Override
	@Transactional
	public int update(OrderForm orderForm) {
		// 受注を更新する
		int result = orderDao.update(makeOrder(orderForm));
		orderDetailDao.deleteByOrderId(orderForm.getOrderId());
		orderForm.getOrderDetailList().forEach(orderDetailForm -> orderDetailDao.add(makeOrderDetail(orderForm.getOrderId(), orderDetailForm)));
		return result;
	}

	@Override
	public int changeStatus(String orderId, OrderStatus orderStatus, LocalDate shipDate) {
		// 受注ステータスを変更する
		return orderDao.changeStatus(orderId, orderStatus, shipDate);
	}

	@Override
	public int delete(String orderId) {
		// 受注を削除する
		int result = orderDao.delete(orderId);
		orderDetailDao.deleteByOrderId(orderId);
		return result;
	}

	private Order makeOrder(OrderForm orderForm) {
		//OrderFormのデータをOrderに入れて返す
		Order order = new Order();
		order.setOrderId(orderForm.getOrderId());
		order.setOrderStatus(orderForm.getOrderStatus());
		order.setOrderDate(orderForm.getOrderDate());
		order.setShipDate(orderForm.getShipDate());
		order.setCustomerName(orderForm.getCustomerName());
		order.setCustomerZipcode(orderForm.getCustomerZipcode());
		order.setCustomerAddress(orderForm.getCustomerAddress());
		order.setCustomerTel(orderForm.getCustomerTel());
		order.setOrderAmount(orderForm.getOrderAmount());
		return order;
	}
	
	private OrderDetail makeOrderDetail(String orderId, OrderFormDetail orderFormDetail) {
		//OrderFormのデータをOrderDetailに入れて返す
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setOrderId(orderId);
		orderDetail.setOrderDetailNo(orderFormDetail.getOrderDetailNo());
		orderDetail.setItemId(orderFormDetail.getItemId());
		orderDetail.setItemName(orderFormDetail.getItemName());
		orderDetail.setItemPrice(orderFormDetail.getItemPrice());
		orderDetail.setOrderQuantity(orderFormDetail.getOrderQuantity());
		orderDetail.setOrderDetailAmount(orderFormDetail.getOrderDetailAmount());
		return orderDetail;
	}

	private OrderForm makeOrderForm(Order order, List<OrderDetail> orderDetailList) {
		//OrderとOrderDetailのデータをOrderFormに入れて返す
		OrderForm orderForm = new OrderForm();
		orderForm.setOrderId(order.getOrderId());
		orderForm.setOrderStatus(order.getOrderStatus());
		orderForm.setOrderDate(order.getOrderDate());
		orderForm.setShipDate(order.getShipDate());
		orderForm.setCustomerName(order.getCustomerName());
		orderForm.setCustomerZipcode(order.getCustomerZipcode());
		orderForm.setCustomerAddress(order.getCustomerAddress());
		orderForm.setCustomerTel(order.getCustomerTel());
		orderForm.setOrderAmount(order.getOrderAmount());
		
		List<OrderFormDetail> orderFormDetailList = new ArrayList<OrderFormDetail>();
		orderDetailList.forEach(orderDetail -> {
			OrderFormDetail orderFormDetail = new OrderFormDetail();
			orderFormDetail.setOrderDetailNo(orderDetail.getOrderDetailNo());
			orderFormDetail.setItemId(orderDetail.getItemId());
			orderFormDetail.setItemName(orderDetail.getItemName());
			orderFormDetail.setItemPrice(orderDetail.getItemPrice());
			orderFormDetail.setOrderQuantity(orderDetail.getOrderQuantity());
			orderFormDetail.setOrderDetailAmount(orderDetail.getOrderDetailAmount());
			orderFormDetailList.add(orderFormDetail);
		});
		orderForm.setOrderDetailList(orderFormDetailList);

		return orderForm;
	}
}
