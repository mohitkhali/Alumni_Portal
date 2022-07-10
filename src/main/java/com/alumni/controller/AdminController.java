package com.alumni.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alumni.email.Email;
import com.alumni.email.EmailSender;
import com.alumni.entities.ContactUs;
import com.alumni.entities.Event;
import com.alumni.entities.User;
import com.alumni.entities.UserExcelExporter;
import com.alumni.entities.UserFullInfo;
import com.alumni.repository.ContactUsRepository;
import com.alumni.repository.EventRepository;
import com.alumni.repository.UserFullInfoRepository;
import com.alumni.repository.UserRepository;
import com.alumni.service.UserService;
import com.alumni.token.ConfirmationToken;


@Controller
@RequestMapping("admin/")

public class AdminController {
	

	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	EmailSender emailSender;
	
	@Autowired
	ContactUsRepository contactUsRepository;
	
	ConfirmationToken confirmationToken;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserFullInfoRepository userFullInfoRepository;
	
	@GetMapping("/verifiedList/{page}")
	public  String unverifiedList(@PathVariable("page") Integer page,Model model,Principal principal) {
		
		String email =principal.getName();
	    Optional<User> user1= userRepository.findByEmail(email);
	    User user= user1.get();
	    Pageable pageable = PageRequest.of(page, 5);
		Page<User> verifiedUsers=userRepository.findAllverfiedUsers(pageable);
		model.addAttribute("userlist", verifiedUsers);
		model.addAttribute("currentpage",page);
		model.addAttribute("totalpages",verifiedUsers.getTotalPages());
		model.addAttribute("title","verfied-List");
		return "/admin/verifiedUsers";
	}
	
	
	
	
	
	
	
	
	@GetMapping("/unverifiedList/{page}")
public  String verifiedList(@PathVariable("page") Integer page,Model model,Principal principal) {
		
		String email =principal.getName();
	    Optional<User> user1= userRepository.findByEmail(email);
	    User user= user1.get();
	    Pageable pageable = PageRequest.of(page, 5);
		Page<User> unverifiedUsers=userService.unverfiedUsers(pageable);
		model.addAttribute("userlist", unverifiedUsers);
		model.addAttribute("currentpage",page);
		model.addAttribute("totalpages",unverifiedUsers.getTotalPages());
		model.addAttribute("title","unverfied-List");
		return "/admin/verifiedUsers";
	}
	
	
	@GetMapping("/viewevents/{page}")
	public String viewEevtns(@PathVariable("page") Integer page,Model model) {
		Pageable pageable = PageRequest.of(page, 2);
		Page<Event> events = eventRepository.findAll(pageable);
		model.addAttribute("events",events);
		model.addAttribute("title","events");
		model.addAttribute("currentpage",page);
		model.addAttribute("totalpages",events.getTotalPages());
		model.addAttribute("title","unverfied-List");
		return "/admin/viewevents";
	}
	
	
	
	@GetMapping("/feedback/{page}")
	public String feedback(@PathVariable("page") Integer page,Model model) {
		 Pageable pageable = PageRequest.of(page, 2);
		 Page<ContactUs> feedback=contactUsRepository.findAll(pageable);
	      model.addAttribute("contact",feedback);
	      model.addAttribute("currentpage",page);
	      model.addAttribute("totalpages",feedback.getTotalPages());
	      model.addAttribute("title","feedback");
		return "/admin/feedback";
	}
	

	@GetMapping("/dashboard")
	public String viewAdminDashBoard(Model model) {
		List<User> unverifiedUsers=userRepository.findAllUnverfiedUsers();
		List<User> verifiedUsers=userRepository.findAllVerfiedUsers();
		 int verfiedUsersNumber=verifiedUsers.size();
		 System.out.print(verfiedUsersNumber);
	     int unverfiedUsersNumber=unverifiedUsers.size();
		model.addAttribute("unvN",unverfiedUsersNumber);
		model.addAttribute("vN",verfiedUsersNumber);
		model.addAttribute("tN",userService.allUsersNumber());
		model.addAttribute("title","dashboard");
	return "/admin/dash";
	}
	
	
	
	@GetMapping("/user/delete/{UId}")
	public String delteUser(@PathVariable("UId")Long uId,Model model) {
		Optional<UserFullInfo> userFullInfo1= userFullInfoRepository.findById(uId);
		
	    UserFullInfo userFullInfo = userFullInfo1.get();
		User user = userFullInfo.getUser();
		Long id= user.getUId();
	    userFullInfo.setUser(null);
		userFullInfoRepository.delete(userFullInfo);
		userRepository.deleteById(id);
		return "redirect:/unverifiedList/0";
	}
	
	@GetMapping("user/profile/{UId}")
	public String viewUser(@PathVariable("UId")Long uId,Model model) {
		Optional<User> user1=userRepository.findById(uId);
		User user= user1.get();
	    List<UserFullInfo> userFullInfo	= userFullInfoRepository.findByUser(user);
	    System.out.print(userFullInfo);   
		model.addAttribute("userfullinfo",userFullInfo);
		model.addAttribute("user",user);
		if(user.getLocked()==true) {
			return "/admin/verifiedprofile";
		}
		return "/admin/userprofile";
	}
	
	@GetMapping("/eventdetails/{evid}")
	public String evetsDetails(@PathVariable("evid")Long evid,Model model)
	{
	Optional<Event> eventOptional=eventRepository.findById(evid);
	Event event= eventOptional.get();
	model.addAttribute("event",event);
	model.addAttribute("title",event.getEventName());
		return "/admin/eventdetails";
	}
	
	
	@GetMapping("/deleteevent/{evid}")
	public String delteEvent(@PathVariable("evid")Long evid,HttpSession session) {
		eventRepository.deleteById(evid);
		session.setAttribute("message", "Event has been deleted successfully");
		return "redirect:/admin/dashboard";
	}
	
	
	@GetMapping("/users")
	public String viewTotalUser(Model model) {
		List<User> totalUser=userService.totalUser();
		model.addAttribute("userlist",totalUser);
		return "/admin/user";
	}
	
	
	@GetMapping("/verify/{UId}")
	public String verifyUser(@PathVariable("UId") Long uId,Model model) {
		userService.unlockUser(uId);
	    User user =	userRepository.getById(uId);
	    String verfiyMsg= "your email has been verfied by admin now you can login "+"http://localhost:8085/login";
		emailSender.send(user.getEmail(), verfiyMsg);
		
		return "redirect:/admin/unverifiedList/0";
		
	}
	
	
	@GetMapping("/email")
	public String sendEmailToAllAccountPage(Model model) {
		model.addAttribute("title", "send email");
		
		return "/admin/email";
		
	}
	@PostMapping("/email")
	public String sendEmailToAllAccounts(@ModelAttribute("email")Email email,HttpSession session) {
		String message = email.getMessage();
	  List<String>  users=  userRepository.findAllEmails();
	  Object[] usersarr =  users.toArray();
	   System.out.println(users);
	  
	  for(int i=0;i<usersarr.length;i++) {
		emailSender.send((String)usersarr[i], message);	
		
	}
	  session.setAttribute("message", "Email has been sent !");
		return "redirect:/admin/dashboard";
	}
	
	
	
	@GetMapping("/Event")
	public String postEvent(Model model) {
		model.addAttribute("title","post-event");
		
		return "/admin/addevent";
	}
	
	
	@PostMapping("/postEvent")
	public String savepostEvent(@RequestParam("eventImage")MultipartFile file,@ModelAttribute("event") Event event,HttpSession session) {
		
		System.out.println("method working");
          try {		
			String fileName =file.getOriginalFilename();
			
			System.out.println(fileName);
			
			event.setBanner(fileName);
				
			File saveFile =	new ClassPathResource("static/images/").getFile();
			
		    Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());

			Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
			eventRepository.save(event);
			session.setAttribute("message", "your Event has been posted  , add more..");
		}
          
			catch (Exception e) {
				
				System.out.print(e.getMessage());
			}
		
		return "redirect:/admin/dashboard";
	}
	
	
//	exporting users!
	
	@GetMapping("/export")
	public void  exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		String headerKey= "Content-Disposition";
		DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime= dateFormat.format(new Date());
		String fileName= "users_"+currentDateTime+".xlsx";
		String headerValue="attachement; filename="+fileName+".xlsx";
	    response.setHeader(headerKey, headerValue);
		System.out.println(headerKey+headerValue);
		List<User> listUsers = userService.listAll();
	
		UserExcelExporter excelExporter= new UserExcelExporter(listUsers);
		excelExporter.export(response);
	
	}

	
	@GetMapping("/addUser")
	public String addUser(Model model) {
		return "/admin/addUser";
	}
	

}
