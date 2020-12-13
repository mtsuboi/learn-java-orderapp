package com.example.orderapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.orderapp.model.Item;

public interface ItemDao {
	// 商品を全件抽出する
	public List<Item> findAll();

	// 商品を全件抽出する(ページング対応)
	public Page<Item> findAll(final Pageable pageable);

	// 商品をitem_idで検索する
	public Optional<Item> findById(final String itemId);

	// 商品を商品名(部分一致)で抽出する
	public List<Item> findByName(final String itemName);

	// 商品を商品名(部分一致)で抽出する(ページング対応)
	public Page<Item> findByName(final String itemName, final Pageable pageable);

	// 商品を追加する
	public void add(final Item item);

	// 商品を更新する
	public int update(final Item item);

	// 商品を削除する
	public int delete(final String itemId);

}
