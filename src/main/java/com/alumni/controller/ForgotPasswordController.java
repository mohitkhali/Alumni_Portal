package com.alumni.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.alumni.email.EmailSender;
import com.alumni.entities.User;
import com.alumni.service.UserService;
import com.alumni.helper.exception.*;

import net.bytebuddy.utility.RandomString;

@Controller
public class ForgotPasswordController {

	@Autowired
	UserService userService;
	
	@Autowired
	EmailSender emailSender;
	

	

	
}
