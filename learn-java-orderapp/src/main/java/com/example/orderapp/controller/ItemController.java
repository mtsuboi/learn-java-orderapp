package com.example.orderapp.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.orderapp.form.ItemForm;
import com.example.orderapp.logic.ItemLogic;
import com.example.orderapp.model.Item;

@Controller
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemLogic itemLogic;

	@GetMapping
	public String list(Model model) {
		// 商品を全件抽出してlistへ入れる
		List<Item> list = itemLogic.findAll();
		model.addAttribute("list", list);
		return "item/item_list";
	}
	
	@GetMapping("/new")
	public String newForm(ItemForm itemForm, Model model) {
		// 新規フォーム
		itemForm.setNewItem(true);
		model.addAttribute("itemForm", itemForm);
		return "item/item_form";
	}

	@PostMapping("/add")
	public String add(@Valid @ModelAttribute ItemForm itemForm, BindingResult result, Model model) {
		// 登録処理
		// ItemFormのデータをItemに格納
		Item item = makeItem(itemForm);
		if(!result.hasErrors()) {
			// エラーが無ければ登録してリストにリダイレクト
			itemLogic.add(item);
			return "redirect:/item";
		} else {
			model.addAttribute("itemForm", itemForm);
			return "/item/item_form";
		}
	}
	
	@GetMapping("/edit/{itemId}")
	public String editForm(ItemForm itemForm, @PathVariable String itemId, Model model) {
		// 編集フォーム
		// 指定された商品を取得
		Optional<Item> optItem = itemLogic.findById(itemId);
		Optional<ItemForm> optItemForm = optItem.map(t -> makeItemForm(t));		
		if(optItem.isPresent()) {
			itemForm = optItemForm.get();
		}
		model.addAttribute("itemForm", itemForm);
		return "item/item_form";
	}

	@PostMapping("/update")
	public String update(@Valid @ModelAttribute ItemForm itemForm, BindingResult result, Model model) {
		// 更新処理
		// ItemFormのデータをItemに格納
		Item item = makeItem(itemForm);
		if(!result.hasErrors()) {
			// エラーが無ければ更新してリストにリダイレクト
			itemLogic.update(item);
			return "redirect:/item";
		} else {
			model.addAttribute("itemForm", itemForm);
			return "/item/item_form";
		}
	}

	@PostMapping("/delete")
	public String delete(@RequestParam("itemId") String itemId, Model model) {
	    // 削除処理
		itemLogic.delete(itemId);
	    return "redirect:/item";
	}


	private Item makeItem(ItemForm itemForm) {
		//ItemFormのデータをItemに入れて返す
		Item item = new Item();
		item.setItemId(itemForm.getItemId());
		item.setItemName(itemForm.getItemName());
		item.setItemPrice(itemForm.getItemPrice());
		
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
