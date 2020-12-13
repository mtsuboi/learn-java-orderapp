package com.example.orderapp.logic;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.orderapp.form.ItemForm;

public interface ItemLogic {
	// 商品を全件抽出する
	public List<ItemForm> findAll();

	// 商品を全件抽出する(ページング対応)
	public Page<ItemForm> findAll(final Pageable pageable);

	// 商品をitem_idで検索する
	public Optional<ItemForm> findById(final String itemId);

	// 商品を商品名(部分一致)で検索する
	public List<ItemForm> findByName(final String itemName);

	// 商品を商品名(部分一致)で検索する(ページング対応)
	public Page<ItemForm> findByName(final String itemName, final Pageable pageable);

	// 商品を追加する
	public void add(final ItemForm itemForm);

	// 商品更新をする
	public int update(final ItemForm itemForm);

	// 商品を削除する
	public int delete(final String itemId);
}
