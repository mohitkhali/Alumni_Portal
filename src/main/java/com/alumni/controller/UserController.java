package com.alumni.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.Multipart;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alumni.email.EmailSender;
import com.alumni.entities.Event;
import com.alumni.entities.Jobs;
import com.alumni.entities.Messages;
import com.alumni.entities.User;
import com.alumni.entities.UserFullInfo;
import com.alumni.repository.EventRepository;
import com.alumni.repository.JobRepository;
import com.alumni.repository.MessageRepository;
import com.alumni.repository.UserFullInfoRepository;
import com.alumni.repository.UserRepository;
import com.alumni.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	EmailSender emailSender;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserFullInfoRepository userFullInfoRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	MessageRepository messageRepository;
	
	
	
	public String getUserName(Principal principal) {
		String email = principal.getName();
		Optional<User> userOptional  = userRepository.findByEmail(email);
		User user = userOptional.get();
		return user.getFullName();
	}
	public User getPricipalUser(Principal principal) {
		String email = principal.getName();
		Optional<User> userOptional  = userRepository.findByEmail(email);
		User user = userOptional.get();
		return user;
	}
	
	
public String getUserImage(Principal principal) {
	User user=getPricipalUser(principal);
	UserFullInfo userInfo = userFullInfoRepository.findByUserId(user);
	return userInfo.getProfileImage();
	}
	
	@GetMapping("/dashboard")
	public String userDashBoard(Model model,Principal principal) {
		String userName=getUserName(principal);
		model.addAttribute("username",userName);
		 model.addAttribute("title","Dashboard");
		 model.addAttribute("profileImage", getUserImage(principal));
		return "/user/userdashbord";
	}
	
	
	
	
	
	@GetMapping("/search")
	public String searchPage(Model model,Principal principal,
			@RequestParam("name")String name
			) {
		
	  if(name=="")
		  
			return "/404";
	  
	  
	  String email = principal.getName();
	   Optional<User> userOptional  = userRepository.findByEmail(email);
	   User user = userOptional.get();
	   if(user.getFullName().matches(name))
		   model.addAttribute("you","you");
	 
	  List<User> users=	userRepository.findByFullNameContaining(name);
	  if(users.size()!=0) {
	  List<UserFullInfo> userFullInfoList= new ArrayList<>();
	  for(int i=0;i<users.size();i++) {
	     UserFullInfo usersFullInfo = userFullInfoRepository.findByUserId(users.get(i));
	     userFullInfoList.add(usersFullInfo);
	  }
	
	 
        model.addAttribute("user",users);
	    model.addAttribute("userfullinfolist",userFullInfoList);
        model.addAttribute("title","search\t"+name);
        model.addAttribute("username",user.getFullName());
        model.addAttribute("profileImage", getUserImage(principal));
		return "/user/search";
	
	  }
	  else {
		  model.addAttribute("profileImage", getUserImage(principal));
		  return "/user/search404";
	  }
	}
	
	@GetMapping("/events/{page}")
	public String viewEevtns(@PathVariable("page") Integer page,Model model,Principal principal) {
		
		String userName=getUserName(principal);
		model.addAttribute("username",userName);	
		Pageable pageable = PageRequest.of(page, 4);
		Page<Event> events = eventRepository.findAll(pageable);
		model.addAttribute("events",events);
		model.addAttribute("title","events");
		model.addAttribute("currentpage",page);
		model.addAttribute("totalpages",events.getTotalPages());
		model.addAttribute("title","events");
		model.addAttribute("profileImage", getUserImage(principal));
		
		return "/user/viewevents";
	}
	
	
	
	
	@GetMapping("/events/")
	public String blankEvents(Principal principal,Model model) {	
		String userName=getUserName(principal);
		model.addAttribute("username",userName);	
		model.addAttribute("profileImage", getUserImage(principal));
			return "redirect:/user/events/0";
	}
	
	
	@GetMapping("/eventdetails/{evid}")
	public String evetsDetails(@PathVariable("evid")Long evid,Model model,Principal principal)
	{
		String userName=getUserName(principal);
		model.addAttribute("username",userName);
	Optional<Event> eventOptional=eventRepository.findById(evid);
	Event event= eventOptional.get();
	model.addAttribute("event",event);
	model.addAttribute("title",event.getEventName());
	model.addAttribute("profileImage", getUserImage(principal));
		return "/user/eventdetails";
	}
	
	
	@GetMapping("/jobs")
	public String jobs(Model model,Principal principal){
		String userName=getUserName(principal);
		model.addAttribute("username",userName);	
	   List<Jobs>job= jobRepository.findAll();
		model.addAttribute("job",job);
		model.addAttribute("title","jobs");
		model.addAttribute("profileImage", getUserImage(principal));
		return "/user/jobs";
	}
	
	@GetMapping("/postJob")
	public String getPostJob(Model model,Principal principal) {
		String userName=getUserName(principal);
	User user=getPricipalUser(principal);
	
		model.addAttribute("username",userName);	
		model.addAttribute("title","post-job");
		model.addAttribute("profileImage", getUserImage(principal));
		return "/user/postjob";
	}
	
	@PostMapping("/job")
	public String postJob(@ModelAttribute("job")Jobs job,Principal principal,Model model) {	
		String userName=getUserName(principal);
		model.addAttribute("username",userName);	
		User user =getPricipalUser(principal);
		job.setUser(user);
		jobRepository.save(job);
		model.addAttribute("profileImage", getUserImage(principal));
		return "redirect:/user/jobs";
	}
	
//	profile section
	@GetMapping("/profile")
	public String loggedInUserProfile(Principal principal,Model model) {
		String userName=getUserName(principal);
		model.addAttribute("username",userName);	
		User user= getPricipalUser(principal);
		List<UserFullInfo> userFullInfo =userFullInfoRepository.findByUser(user);
		model.addAttribute("userFullInfo",userFullInfo);
		model.addAttribute("title",user.getFullName());
		model.addAttribute("profileImage", getUserImage(principal));
		return "/user/profileDetails";
	}
	
	@GetMapping("/delete/{UId}")
	public String delteUser(@PathVariable("UId")Long uId,Model model,Principal principal) {
		String userName=getUserName(principal);
		model.addAttribute("username",userName);	
		Optional<UserFullInfo> userFullInfo1= userFullInfoRepository.findById(uId);
		
	    UserFullInfo userFullInfo = userFullInfo1.get();
		User user = userFullInfo.getUser();
		Long id= user.getUId();
	    userFullInfo.setUser(null);
		userFullInfoRepository.delete(userFullInfo);
		userRepository.deleteById(id);
		model.addAttribute("profileImage", getUserImage(principal));
		return "redirect:/Home";
	}
	
	@GetMapping("/update-profile")
	public String updateProfile(Model model,Principal principal) {
		String userName=getUserName(principal);
		model.addAttribute("username",userName);	
	User user =	getPricipalUser(principal);
	UserFullInfo userFullInfo = userFullInfoRepository.findByUserId(user);
	model.addAttribute("userFullInfo",userFullInfo);
	model.addAttribute("title","update profile of"+user.getFullName());	
	model.addAttribute("profileImage", getUserImage(principal));
		return "/user/updateProfile";
	}
	
	@PostMapping("/update-profile")
	public String profileUpdateHandler(@ModelAttribute("userFullInfo")UserFullInfo userFullInfo,@ModelAttribute("user")User userOb ,Principal principal,@RequestParam("userProfileImage")MultipartFile file,
			HttpSession session) {
		
		
		User user= getPricipalUser(principal);
        userFullInfo.setUser(user);
        UserFullInfo olduserInfo=userFullInfoRepository.findByUserId(user);
		
		try {
			
			
			if(!file.isEmpty()) {
					
				File saveFile =	new ClassPathResource("static/images/").getFile();
				
			    Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());

				Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
			
				olduserInfo.setProfileImage(file.getOriginalFilename());
			
			}

			else {
				

				olduserInfo.setProfileImage(olduserInfo.getProfileImage());
	
			}
			user.setFullName(userOb.getFullName());
			userFullInfo.setUser(user);		
			olduserInfo.setFatherName(userFullInfo.getFatherName());
			olduserInfo.setMotherName(userFullInfo.getMotherName());
			olduserInfo.setGender(userFullInfo.getGender());
			olduserInfo.setRollNo(userFullInfo.getRollNo());
			olduserInfo.setPassingYear(userFullInfo.getPassingYear());
			olduserInfo.setPermanentAddress(userFullInfo.getPermanentAddress());
			olduserInfo.setCity(userFullInfo.getCity());
			olduserInfo.setState(userFullInfo.getState());
			olduserInfo.setCurrentPos(userFullInfo.getCurrentPos());
			olduserInfo.setPhoneNumber(userFullInfo.getPhoneNumber());
			olduserInfo.setJobProfile(userFullInfo.getJobProfile());
			olduserInfo.setDob(userFullInfo.getDob());
			olduserInfo.setJobLocation(userFullInfo.getJobLocation());
			olduserInfo.setCompanyName(userFullInfo.getCompanyName());
			olduserInfo.setSkills(userFullInfo.getSkills());
			olduserInfo.setBio(userFullInfo.getBio());
			userRepository.save(user);
			userFullInfoRepository.save(olduserInfo);
		}
		
		catch (Exception e) {
			
			System.out.print(e.getMessage());
		}
		session.setAttribute("message", "details updated...");
		
		return "redirect:/user/profile";
	}
	
//	profile section ends here
	@GetMapping("/setting")
	public String setting(Model model,Principal principal) {
		String userName=getUserName(principal);
		model.addAttribute("username",userName);	
				model.addAttribute("title","change-password");
				model.addAttribute("profileImage", getUserImage(principal));
		return "/user/changePassword";
	}
	
	
	
	
	@PostMapping("/setting")
	public String changePassword(@RequestParam("oldPassword") String oldpass,@RequestParam("newPassword") String newpass,Principal principal,HttpSession session,Model model) {
	User user=getPricipalUser(principal);
	model.addAttribute("title","change-password");
	String userName=getUserName(principal);
	model.addAttribute("username",userName);	
	if(this.bCryptPasswordEncoder.matches(oldpass, user.getPassword()))	{
		user.setPassword(this.bCryptPasswordEncoder.encode(newpass));
		userRepository.save(user);
		model.addAttribute("profileImage", getUserImage(principal));
		session.setAttribute("msg", "password-changed");
		return "redirect:/user/dashboard";
	}
	else {
		session.setAttribute("msg", "check old password!");
		model.addAttribute("profileImage", getUserImage(principal));
		return "/user/changePassword";
	}
	
				
}
	
	
	@GetMapping("/send-message/{uid}")
	public String sendMessage(@PathVariable("uid")Long uId,Model model,Principal principal) {
		model.addAttribute("profileImage", getUserImage(principal));
		model.addAttribute("uid"+uId);
		model.addAttribute("title","send message");
		return "/user/messages";
	}
	
	@PostMapping("/send-message/{uid}")
	public String postmessage(@PathVariable("uid")Long uId,@ModelAttribute("message")Messages msg,Model model,Principal principal,HttpSession session) {
	User user=userRepository.findById(uId).get();
	User priUser= getPricipalUser(principal);
	msg.setUser(user);
	emailSender.send(user.getEmail(),priUser.getFullName()+"\t"+"send you a message"+"\n"+msg.getMsg());
	messageRepository.save(msg);
	session.setAttribute("msg", "your message has been sent to"+user.getFullName());
		return "redirect:/user/dashboard";
	}
	
	
	
}
	
	

