package com.alumni.controller;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alumni.entities.ContactUs;
import com.alumni.helper.Message;
import com.alumni.repository.ContactUsRepository;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomeController {
	
	ContactUsRepository contactUsRepository;
	
	@RequestMapping("/home")
	public String homepage(){
		return "index";
	}
	
	@RequestMapping("/services")
	public String services(){
		return "services";
	}
	
	@RequestMapping("/contact")
	public String contact(Model model){
		model.addAttribute("contactUs",new ContactUs());
		return "contact";
	}
	
	
	@PostMapping("/contact")
	public String saveContactUsResponse(@ModelAttribute ContactUs contact,BindingResult bindingResult,
			Model model,HttpSession session
			) {
		
	
		if(bindingResult.hasErrors()) {
		
			 return "redirect:/contact";
		}
		contactUsRepository.save(contact);
			return "ThankYou";
	}
	
	@GetMapping("/error")
	public String handleError() {
		return "error";
	}

}
