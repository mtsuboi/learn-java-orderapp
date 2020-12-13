package com.example.orderapp.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

import com.example.orderapp.constants.OrderStatus;
import com.example.orderapp.form.OrderForm;
import com.example.orderapp.form.OrderFormDetail;
import com.example.orderapp.logic.OrderLogic;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderLogic orderLogic;
	
	@GetMapping
	public String list(@PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(name = "orderStatus", required = false) Optional<String> optOrderStatus,
			Model model) {
		OrderStatus orderStatus = OrderStatus.ORDER;
		if(optOrderStatus.isPresent()) {
			orderStatus = OrderStatus.getByCode(optOrderStatus.get());
		}
		Page<OrderForm> page = orderLogic.findByStatus(orderStatus, pageable);
		model.addAttribute("orderStatusList",OrderStatus.values());
		model.addAttribute("orderStatus", orderStatus);
		model.addAttribute("page", page);
		return "order/order_list";
	}

	@GetMapping("/new")
	public String newForm(OrderForm orderForm, Model model) {
		// 新規フォーム
		orderForm.setNewItem(true);
		orderForm.setOrderStatus(OrderStatus.ORDER);
		orderForm.setOrderDate(LocalDate.now());
		// 明細の1行目だけ空行を用意しておく
		OrderFormDetail orderFormDetail = new OrderFormDetail();
		orderFormDetail.setOrderDetailNo(1);
		List<OrderFormDetail> orderDetailList = new ArrayList<OrderFormDetail>();
		orderDetailList.add(orderFormDetail);
		orderForm.setOrderDetailList(orderDetailList);
		model.addAttribute("orderForm", orderForm);
		return "order/order_form";
	}

	@GetMapping("/edit/{orderId}")
	public String editForm(OrderForm orderForm, @PathVariable String orderId, Model model) {
		// 編集フォーム
		// 指定された商品を取得
		Optional<OrderForm> optOrderForm = orderLogic.findById(orderId);
		if(optOrderForm.isPresent()) {
			// 該当商品が検索できたらフォームを表示
			orderForm = optOrderForm.get();
			orderForm.setNewItem(false);
			model.addAttribute("orderForm", orderForm);
			return "order/order_form";
		} else {
			// 該当受注が検索できなかった場合エラーページへ
			model.addAttribute("message", "指定された受注は存在しません。別のユーザーに削除された可能性があります。");
			return "error";
		}
	}

	@PostMapping("/save")
	public String update(@Valid @ModelAttribute OrderForm orderForm, BindingResult result, Model model) {
		// 更新処理
		if(!result.hasErrors()) {
			// エラーが無ければ登録または更新してリストにリダイレクト
			if(orderForm.isNewItem()) {
				try {
					orderLogic.add(orderForm);
				} catch (DuplicateKeyException e) {
					// キー重複の場合はエラーをセットしてフォームを表示
					result.rejectValue("orderId", "", "既に登録済みの受注IDです。");
					model.addAttribute("orderForm", orderForm);
					return "/order/order_form";
				}
			} else {
				orderLogic.update(orderForm);				
			}
			return "redirect:/order?orderStatus=" + orderForm.getOrderStatus().getCode();
		} else {
			model.addAttribute("orderForm", orderForm);
			return "order/order_form";
		}
	}

	@PostMapping("/changestatus")
	public String changeStatus(@RequestParam("orderId") String orderId, @RequestParam("orderStatus") String strOrderStatus, Model model) {
	    // ステータス更新処理
		OrderStatus orderStatus = OrderStatus.valueOf(strOrderStatus);
		LocalDate shipDate = null;
		if(orderStatus == OrderStatus.SHIPPED) {
			shipDate = LocalDate.now();
		}
		orderLogic.changeStatus(orderId, orderStatus, shipDate);
	    return "redirect:/order?orderStatus=" + orderStatus.getCode();
	}

	@PostMapping("/delete")
	public String delete(@RequestParam("orderId") String orderId, Model model) {
	    // 削除処理
		orderLogic.delete(orderId);
	    return "redirect:/order";
	}
}
