package com.example.orderapp.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class OrderFormDetail {
	private Integer orderDetailNo;

	@NotBlank(message = "商品IDは必須です。")
	@Pattern(regexp = "^[A-Z0-9]{5}$", message = "商品IDは半角5桁(英大文字または数字)で入力してください。")
	private String itemId;

	@NotBlank(message = "商品名は必須です。")
	private String itemName;

	@NotNull(message = "単価は必須です。")
	@Min(0)
	private Integer itemPrice;

	@NotNull(message = "数量は必須です。")
	@Min(1)
	private Integer orderQuantity;

	@NotNull(message = "金額は必須です。")
	@Min(0)
	private Integer orderDetailAmount;

	public Integer getOrderDetailNo() {
		return orderDetailNo;
	}
	public void setOrderDetailNo(Integer orderDetailNo) {
		this.orderDetailNo = orderDetailNo;
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
	public Integer getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Integer itemPrice) {
		this.itemPrice = itemPrice;
	}
	public Integer getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public Integer getOrderDetailAmount() {
		return orderDetailAmount;
	}
	public void setOrderDetailAmount(Integer orderDetailAmount) {
		this.orderDetailAmount = orderDetailAmount;
	}

}
