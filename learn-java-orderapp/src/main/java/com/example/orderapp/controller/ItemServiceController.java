package com.example.orderapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderapp.form.ItemForm;
import com.example.orderapp.logic.ItemLogic;

@RestController
@RequestMapping("/itemService")
public class ItemServiceController {
	@Autowired
	private ItemLogic itemLogic;

	@GetMapping("/findById")
	public ItemForm findById(@RequestParam("itemId") String itemId) {
		// 指定された商品を取得して返却
		Optional<ItemForm> optItemForm = itemLogic.findById(itemId);
		if(optItemForm.isPresent()) {
			return optItemForm.get();
		} else {
			return null;
		}
	}

	@GetMapping("/findByName")
	public Page<ItemForm> findByName(
			@RequestParam("itemName") String itemName,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		// 指定された商品名に該当する商品リストを取得して返却
		return itemLogic.findByName(itemName, pageable);
	}
}
