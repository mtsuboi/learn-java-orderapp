package com.example.orderapp.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.orderapp.form.ItemForm;
import com.example.orderapp.model.Item;
import com.example.orderapp.repository.ItemDao;

@Service
public class ItemLogicImpl implements ItemLogic {
	@Autowired
	private ItemDao itemDao;

	@Override
	public List<ItemForm> findAll() {
		// 商品を全件抽出する
		List<ItemForm> list = new ArrayList<ItemForm>();
		itemDao.findAll().forEach(item -> list.add(makeItemForm(item)));
		return list;
	}
	
	@Override
	public Page<ItemForm> findAll(Pageable pageable) {
		// 商品を全件抽出する(ページング対応)
		List<ItemForm> list = new ArrayList<ItemForm>();
		Page<Item> page = itemDao.findAll(pageable);
		page.getContent().forEach(item -> list.add(makeItemForm(item)));
		return new PageImpl<ItemForm>(list, pageable, page.getTotalElements());
	}

	@Override
	public Optional<ItemForm> findById(String itemId) {
		// 商品をitem_idで検索する
		return itemDao.findById(itemId).map(item -> makeItemForm(item));
	}
	
	@Override
	public List<ItemForm> findByName(String itemName) {
		// 商品を商品名(部分一致)で検索する
		List<ItemForm> list = new ArrayList<ItemForm>();
		itemDao.findByName(itemName).forEach(item -> list.add(makeItemForm(item)));
		return list;
	}

	@Override
	public Page<ItemForm> findByName(String itemName, Pageable pageable) {
		// 商品を商品名(部分一致)で検索する(ページング対応)
		List<ItemForm> list = new ArrayList<ItemForm>();
		Page<Item> page = itemDao.findByName(itemName, pageable);
		page.getContent().forEach(item -> list.add(makeItemForm(item)));
		return new PageImpl<ItemForm>(list, pageable, page.getTotalElements());
	}

	@Override
	@Transactional
	public void add(ItemForm itemForm) {
		// 商品を追加する
		itemDao.add(makeItem(itemForm));
	}

	@Override
	@Transactional
	public int update(ItemForm itemForm) {
		// 商品を更新する
		return itemDao.update(makeItem(itemForm));
	}
	
	@Override
	@Transactional
	public int delete(String itemId) {
		// 商品を削除する
		return itemDao.delete(itemId);
	}

	private Item makeItem(ItemForm itemForm) {
		//ItemFormのデータをItemに入れて返す
		Item item = new Item();
		item.setItemId(itemForm.getItemId());
		item.setItemName(itemForm.getItemName());
		item.setItemPrice(itemForm.getItemPrice().intValue());
		
		return item;
	}
	
	private ItemForm makeItemForm(Item item) {
		//ItemのデータをItemFormに入れて返す
		ItemForm itemForm = new ItemForm();
		itemForm.setItemId(item.getItemId());
		itemForm.setItemName(item.getItemName());
		itemForm.setItemPrice(item.getItemPrice());
		itemForm.setNewItem(false);
		
		return itemForm;
	}
}
