package com.example.orderapp.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.orderapp.logic.ItemLogicException;

@ControllerAdvice
public class GlobalControllerAdvice {
	
	@ExceptionHandler(ItemLogicException.class)
	public String handleException(ItemLogicException e,Model model) {
		// ItemLogicException発生でエラーページへ
		model.addAttribute("message", e);
		return "error";
	}
}
