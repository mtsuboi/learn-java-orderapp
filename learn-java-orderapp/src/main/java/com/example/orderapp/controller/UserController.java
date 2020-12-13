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

import com.example.orderapp.constants.Role;
import com.example.orderapp.form.UserForm;
import com.example.orderapp.logic.UserLogic;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserLogic userLogic;

	@GetMapping
	public String list(@PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(name = "userSearch", required = false) Optional<String> optUserSearch,
			Model model) {
		Page<UserForm> page = null;
		String userSearch = "";
		if(optUserSearch.isPresent()) {
			userSearch = optUserSearch.get();
			page = userLogic.findByName(userSearch, pageable);
		} else {
			// ユーザーを全件抽出
			page = userLogic.findAll(pageable);
		}
		model.addAttribute("userSearch", userSearch);
		model.addAttribute("page", page);
		return "user/user_list";
	}
	
	@GetMapping("/new")
	public String newForm(UserForm userForm, Model model) {
		// 新規フォーム
		userForm.setNewUser(true);
		model.addAttribute("roleList", Role.values());
		model.addAttribute("userForm", userForm);
		return "user/user_form";
	}

	@GetMapping("/edit/{userId}")
	public String editForm(UserForm userForm, @PathVariable String userId, Model model) {
		// 編集フォーム
		// 指定されたユーザーを取得
		Optional<UserForm> optUserForm = userLogic.findById(userId);
		if(optUserForm.isPresent()) {
			// 該当ユーザーが検索できたらフォームを表示
			userForm = optUserForm.get();
			userForm.setNewUser(false);
			model.addAttribute("roleList", Role.values());
			model.addAttribute("userForm", userForm);
			return "user/user_form";
		} else {
			// 該当ユーザーが検索できなかった場合エラーページへ
			model.addAttribute("message", "指定されたユーザーは存在しません。別のユーザーに削除された可能性があります。");
			return "error";
		}
	}

	@PostMapping("/save")
	public String update(@Valid @ModelAttribute UserForm userForm, BindingResult result, Model model) {
		// 更新処理
		if(!result.hasErrors()) {
			// エラーが無ければ登録または更新してリストにリダイレクト
			if(userForm.isNewUser()) {
				try {
					userLogic.add(userForm);
				} catch (DuplicateKeyException e) {
					// キー重複の場合はエラーをセットしてフォームを表示
					result.rejectValue("userId", "", "既に登録済みのユーザーIDです。");
					model.addAttribute("userForm", userForm);
					return "/user/user_form";
				}
			} else {
				userLogic.update(userForm);				
			}
			return "redirect:/user";
		} else {
			model.addAttribute("userForm", userForm);
			return "/user/user_form";
		}
	}

	@PostMapping("/delete")
	public String delete(@RequestParam("userId") String userId, Model model) {
	    // 削除処理
		userLogic.delete(userId);
	    return "redirect:/user";
	}

}
