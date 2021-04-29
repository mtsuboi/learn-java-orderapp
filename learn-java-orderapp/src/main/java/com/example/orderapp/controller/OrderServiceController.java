package com.example.orderapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderapp.model.Order;
import com.example.orderapp.repository.OrderDao;

@RestController
@RequestMapping("/orderService")
public class OrderServiceController {
	@Autowired
	private OrderDao orderDao;

	@GetMapping("/findById")
	public Order findById(@RequestParam("orderId") String orderId) {
		// 指定された受注を取得して返却
		Optional<Order> optOrder = orderDao.findById(orderId);
		if(optOrder.isPresent()) {
			return optOrder.get();
		} else {
			return null;
		}
	}
	
	@PostMapping("/changeStatus")
	public int changeStatus(@RequestBody Order order) {
		return orderDao.changeStatus(order.getOrderId(), order.getOrderStatus(), order.getShipDate());
	}

}
