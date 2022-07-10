package com.alumni.controller;


import com.alumni.email.EmailSender;
import com.alumni.entities.User;
import com.alumni.helper.Message;
import com.alumni.helper.RegistrationRequest;
import com.alumni.helper.exception.UserNotFoundException;
import com.alumni.repository.UserRepository;
import com.alumni.service.UserRegistrationService;
import com.alumni.service.UserService;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@Controller
public class UserRegistrationController {

    @Autowired
    UserRegistrationService userRegistrationService;

    EmailSender emailSender;
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;
//    registering user
    
    @GetMapping("/signUp")
    public String register() {
    	return "SignUp";
    }
    
    @PostMapping("/signUp")
    public String register(@ModelAttribute("user") User user,HttpSession session){
    	try {
    		userRegistrationService.register(user);
    		session.setAttribute("message",new Message("confrimation mail has been sent to your email address", "success"));
    		return "SignUp";
    	}
    	catch (Exception e) {
    		session.setAttribute("message",new Message("account already created with this email !", "success"));
    		return "SignUp";
		}
        
    }

//    Confirming user with token
    @GetMapping(path="signUp/confirm")
    public String confirmUser(@RequestParam("token") String token){
    	userRegistrationService.confirmToken(token);
        return "verified";
        
    }

//    retuning login page with get method


    @GetMapping("/login")
    public String loginPage(){
    	
        return
                "login";
    }

//  method for processing login details

	@RequestMapping("/login")
    public String processLogin(Model model,HttpSession session,@ModelAttribute("user") User user){
    	if(user.isEnabled()==false) {
    	
    		System.out.print("workingggggggggggggggggggggggggggggggg......................");
    		
    		session.setAttribute("message",new Message("email is not verified !", "success"));
    		return "login";
    	}
    	
    	return "login";
  
    }

	
	@GetMapping("/forgot_password")
	public String showForgotPasswordForm() {
		return "forgotpasswordform";
	}
	
	
	@PostMapping("/forgot_password")
	public String processForgotPasswordForm(HttpServletRequest httpServletRequest, Model model,HttpSession session) throws Exception {
		String email=httpServletRequest.getParameter("email");
		Optional<User> user= userService.userPresent(email);
		String token=RandomString.make(15);
		try {
		userService.UpdateResetPasswordToken(token, email);
		String resetPasswordLink="http://localhost:8085/reset_password?token="+token;
		emailSender.send(email, resetPasswordLink);
		session.setAttribute("message",new Message("we have sent mail to your account"
				+ "!", "success"));
		}
		catch (Exception e) {
			session.setAttribute("message",new Message("email is not registered !", "success"));
			model.addAttribute("error",e.getMessage());
			return "forgotpasswordform";
		}
		
//		}else{
//		session.setAttribute("message",new Message("email is not registered !", "success"));
//		return "forgotpasswordform";
//		}
		return "forgotpasswordform";
	}

	
	
	@GetMapping("/reset_password")
	public String showresetPasswordform(@Param( value = "token") String token,Model model ) {
		User user= userService.get(token);
		if(user==null) {
			model.addAttribute("message","invalid token");
		return "message";
		}
		model.addAttribute("token",token);
		return "resetpasswordform";
	}
	
	
	@PostMapping("/reset_password")
	public String changepassword(HttpServletRequest request,Model model,HttpSession session) {
		String token =request.getParameter("token");
		String password=request.getParameter("password");
		User user= userService.get(token);
		userService.updatePassword(user, password);
		session.setAttribute("message",new Message("you have sucessfully changed your password!", "success"));
		return "login";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
