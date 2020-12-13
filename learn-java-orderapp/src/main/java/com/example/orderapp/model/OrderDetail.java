package com.example.orderapp.model;

public class OrderDetail {
	private int orderDetailNo;
	private String orderId;
	private String itemId;
	private String itemName;
	private int itemPrice;
	private int orderQuantity;
	private int orderDetailAmount;

	public int getOrderDetailNo() {
		return orderDetailNo;
	}
	public void setOrderDetailNo(int orderDetailNo) {
		this.orderDetailNo = orderDetailNo;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(int itemPrice) {
		this.itemPrice = itemPrice;
	}
	public int getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public int getOrderDetailAmount() {
		return orderDetailAmount;
	}
	public void setOrderDetailAmount(int orderDetailAmount) {
		this.orderDetailAmount = orderDetailAmount;
	}

}
