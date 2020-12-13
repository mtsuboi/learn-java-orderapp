package com.example.orderapp.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

@Controller
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemLogic itemLogic;

	@GetMapping
	public String list(@PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(name = "itemSearch", required = false) Optional<String> optItemSearch,
			Model model) {
		Page<ItemForm> page = null;
		String itemSearch = "";
		if(optItemSearch.isPresent()) {
			itemSearch = optItemSearch.get();
			page = itemLogic.findByName(itemSearch, pageable);
		} else {
			// 商品を全件抽出
			page = itemLogic.findAll(pageable);
		}
		model.addAttribute("itemSearch", itemSearch);
		model.addAttribute("page", page);
		return "item/item_list";
	}
	
	@GetMapping("/new")
	public String newForm(ItemForm itemForm, Model model) {
		// 新規フォーム
		itemForm.setNewItem(true);
		model.addAttribute("itemForm", itemForm);
		return "item/item_form";
	}

	@GetMapping("/edit/{itemId}")
	public String editForm(ItemForm itemForm, @PathVariable String itemId, Model model) {
		// 編集フォーム
		// 指定された商品を取得
		Optional<ItemForm> optItemForm = itemLogic.findById(itemId);
		if(optItemForm.isPresent()) {
			// 該当商品が検索できたらフォームを表示
			itemForm = optItemForm.get();
			itemForm.setNewItem(false);
			model.addAttribute("itemForm", itemForm);
			return "item/item_form";
		} else {
			// 該当商品が検索できなかった場合エラーページへ
			model.addAttribute("message", "指定された商品は存在しません。別のユーザーに削除された可能性があります。");
			return "error";
		}
	}

	@PostMapping("/save")
	public String update(@Valid @ModelAttribute ItemForm itemForm, BindingResult result, Model model) {
		// 更新処理
		if(!result.hasErrors()) {
			// エラーが無ければ登録または更新してリストにリダイレクト
			if(itemForm.isNewItem()) {
				try {
					itemLogic.add(itemForm);
				} catch (DuplicateKeyException e) {
					// キー重複の場合はエラーをセットしてフォームを表示
					result.rejectValue("itemId", "", "既に登録済みの商品IDです。");
					model.addAttribute("itemForm", itemForm);
					return "/item/item_form";
				}
			} else {
				itemLogic.update(itemForm);				
			}
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

}
