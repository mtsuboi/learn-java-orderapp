package com.example.orderapp.logic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.orderapp.model.Item;
import com.example.orderapp.repository.ItemDao;

@Service
public class ItemLogicImpl implements ItemLogic {
	@Autowired
	private ItemDao itemDao;

	@Override
	public List<Item> findAll() {
		// 商品を全件抽出する
		return itemDao.findAll();
	}
	
	@Override
	public Optional<Item> findById(String itemId) {
		// 商品をitem_idで検索する
		return itemDao.findById(itemId);			
	}
	
	@Override
	public List<Item> findByName(String itemName) {
		// 商品を商品名(部分一致)で検索する
		return itemDao.findByName(itemName);
	}

	@Override
	@Transactional
	public void add(Item item) {
		// 商品を追加する
		itemDao.add(item);
	}

	@Override
	@Transactional
	public int update(Item item) {
		// 商品を更新する
		return itemDao.update(item);
	}
	
	@Override
	@Transactional
	public int delete(String itemId) {
		// 商品を削除する
		return itemDao.delete(itemId);
	}
}
