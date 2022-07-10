package com.alumni.controller;

import com.alumni.entities.User;
import com.alumni.entities.UserFullInfo;
import com.alumni.repository.UserRepository;
import com.alumni.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class DefaultSuccessUrl {

	@Autowired
    UserRepository userRepository;
	
	@Autowired
	UserService userService;


	
    @RequestMapping("/default")
    public String defaultSuccessUrl(HttpServletRequest request, Principal principal){
    	System.out.print(principal.getName());
        String email=principal.getName();
      
        Optional<User> user=userRepository.findByEmail(email);
      
        User user1= user.get();
        System.out.print(user1.getRole());
        if(user1.getRole().toString().matches("ROLE_ADMIN")==true)
        	return "redirect:/admin/dashboard";
        	
        else if(user1.getLocked()==false && user1.getFilledForm()==true)
        	return "/admin/wait";
              else if(user1.getLocked()==false && user1.getUserType().matches("student")==true){
        	System.out.print("working........");
            return "/user/StudentProfileDetail";
        }
        else if(user1.getLocked()==false && user1.getUserType().matches("student")==true ){
        	System.out.print("working........");
            return "/user/StudentProfileDetail";
        }
        
        else if(user1.getLocked()==false && user1.getUserType().matches("alumni")==true) {
        	return "/user/AlumniProfileDetail";
        }

        return "redirect:/user/dashboard";
    }


	public DefaultSuccessUrl() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	@PostMapping("/profileDetails")
	public String saveProfileDetails(@RequestParam("userProfileImage")MultipartFile file,@ModelAttribute UserFullInfo userFullInfo
			,
			HttpSession session,		
			Principal principal
			){
		
		String email = principal.getName();
		
		Optional<User> tempUser = userRepository.findByEmail(email); 
		
		User user =tempUser.get();
		
		userFullInfo.setUser(user);
		
		try {
			
			
			if(file.isEmpty()) {
				
			}

			else {
				
//				uploading file!
				
			System.out.println("trying.........");	
				
			String fileName =file.getOriginalFilename();
			
			System.out.println(fileName);
			
			userFullInfo.setProfileImage(fileName);
				
			File saveFile =	new ClassPathResource("static/images/").getFile();
			
		    Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());

			Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
	
			}
			
			userService.saveUserFullInfo(userFullInfo);
			userService.FormFilled(email);
		}
		
		catch (Exception e) {
			
			System.out.print(e.getMessage());
		}
		
		
		
		
		
		
		return "/admin/Wait";
	}

}
