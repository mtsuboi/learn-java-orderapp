package com.example.orderapp.logic;

import java.util.List;
import java.util.Optional;

import com.example.orderapp.model.Item;

public interface ItemLogic {
	// 商品を全件抽出する
	List<Item> findAll();

	// 商品をitem_idで検索する
	Optional<Item> findById(final String itemId);

	// 商品を商品名(部分一致)で検索する
	List<Item> findByName(final String itemName);

	// 商品を追加する
	void add(final Item item);

	// 商品更新をする
	int update(final Item item);

	// 商品を削除する
	int delete(final String itemId);
}
