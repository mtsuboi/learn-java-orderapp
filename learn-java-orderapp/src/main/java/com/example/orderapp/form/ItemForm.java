package com.example.orderapp.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ItemForm {
	@NotBlank(message = "商品IDは必須です。")
	@Pattern(regexp = "^[A-Z0-9]{5}$", message = "商品IDは半角5桁(英大文字または数字)で入力してください。")
	private String itemId;
	
	@NotBlank(message = "商品名は必須です。")
	private String itemName;
	
	@NotNull(message = "単価は必須です。")
	@Min(0)
	private int itemPrice;
	
	public boolean isNewItem;

	public ItemForm() {}

	public ItemForm(String itemId, String itemName, int itemPrice, boolean isNewItem) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemPrice = itemPrice;
		this.isNewItem = isNewItem;
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

	public boolean isNewItem() {
		return isNewItem;
	}

	public void setNewItem(boolean isNewItem) {
		this.isNewItem = isNewItem;
	}
	
}
