package com.tekbista.authentication.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api/v1")
public class ViewController {

	@GetMapping("/passwordReset")
	public ModelAndView passwordReset(@RequestParam("token") String token) {
		ModelAndView view = new ModelAndView();
		view.setViewName("ResetPassword");
		view.addObject("token", token);
		return view;

	}
}
