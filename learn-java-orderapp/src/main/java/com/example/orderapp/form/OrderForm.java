package com.example.orderapp.form;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.orderapp.constants.OrderStatus;

public class OrderForm {
	@NotBlank(message = "受注IDは必須です。")
	@Pattern(regexp = "^[0-9]{6}$", message = "商品IDは半角5桁(数字)で入力してください。")
	private String orderId;

	private OrderStatus orderStatus;

	@NotNull(message = "受注日は必須です。")
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private LocalDate orderDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private LocalDate shipDate;

	@NotBlank(message = "顧客名は必須です。")
	private String customerName;

	@NotBlank(message = "郵便番号は必須です。")
	private String customerZipcode;

	@NotBlank(message = "住所は必須です。")
	private String customerAddress;

	private String customerTel;

	@NotNull(message = "受注金額は必須です。")
	private Integer orderAmount;

	@Valid
	private List<OrderFormDetail> orderDetailList;

	private Boolean isNewItem;

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	public LocalDate getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}
	public LocalDate getShipDate() {
		return shipDate;
	}
	public void setShipDate(LocalDate shipDate) {
		this.shipDate = shipDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerZipcode() {
		return customerZipcode;
	}
	public void setCustomerZipcode(String customerZipcode) {
		this.customerZipcode = customerZipcode;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getCustomerTel() {
		return customerTel;
	}
	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}
	public Integer getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}
	public List<OrderFormDetail> getOrderDetailList() {
		return orderDetailList;
	}
	public void setOrderDetailList(List<OrderFormDetail> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}
	public Boolean isNewItem() {
		return isNewItem;
	}
	public void setNewItem(Boolean isNewItem) {
		this.isNewItem = isNewItem;
	}

}
