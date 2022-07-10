package com.alumni.service;

import com.alumni.email.EmailSender;
import com.alumni.entities.User;
import com.alumni.entities.UserFullInfo;
import com.alumni.entities.role.UserRole;
import com.alumni.helper.exception.UserNotFoundException;
import com.alumni.repository.UserFullInfoRepository;
import com.alumni.repository.UserRepository;
import com.alumni.token.ConfirmationToken;
import com.alumni.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

	@Autowired
    private  UserRepository userRepository;

	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

	

	@Autowired
    private ConfirmationTokenService confirmationTokenService;

	@Autowired
    private  EmailSender emailSender;
    
    @Autowired
    UserFullInfoRepository userFullInfoRepository;

    private final static String USER_NOT_FOUND_MSG=
            "user with %s not found";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException(
                   String.format(USER_NOT_FOUND_MSG,email)
                ));

    }
    
    public java.util.List<User> listAll(){
		java.util.List<User> users=new ArrayList<User>();
		users=userRepository.findAll();
		return users;
	}

  

    public String signUpUser(User user){

       boolean userExist = userRepository.findByEmail(user.getEmail()).isPresent();
       
       if (userExist)
           throw new IllegalStateException("email already exist");

      String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

      user.setPassword(encodedPassword);

      userRepository.save(user);

        String token=  UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
         return token;
    }

//    TODO: Send Email
public int enableAppUser(String email) {

        return userRepository.enableUser(email);
}
public void UpdateResetPasswordToken(String token,String email) throws UserNotFoundException {
	Optional<User> user=userRepository.findByEmail(email);
	User user1=user.get();
	
	if(user1!=null) {
		user1.setResetPasswordToken(token);
		userRepository.save(user1);
	}
	else {
		throw new UserNotFoundException("could not found any user with email "+email);
	}
}


public User get(String resetPasswordToken) {

	return userRepository.findByresetPasswordToken(resetPasswordToken);
}

public void updatePassword(User user, String newpassword) {
	
	BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
	String encodePassword= passwordEncoder.encode(newpassword);
	user.setPassword(encodePassword);
	user.setResetPasswordToken(null);
	userRepository.save(user);
	
}

public Optional<User> userPresent(String email) {
	return userRepository.findByEmail(email);
}

@Bean
public void saveAdmin() {
	com.alumni.entities.User user=new com.alumni.entities.User();
	user.setEmail("mohitkhali2001@gmail.com");
	user.setEnabled(true);
	user.setLocked(true);
	user.setPassword(bCryptPasswordEncoder.encode("123"));
	user.setFullName("admin");
	user.setRole(UserRole.ROLE_ADMIN);
	user.setResetPasswordToken(null);
	userRepository.save(user);
}



public void saveUserFullInfo(UserFullInfo userFullInfo) {
	 userFullInfoRepository.save(userFullInfo);
	 String link ="http://localhost:8085/admin/dashboard";
	   emailSender.send("mohitkhali2001@gmail.com", adminEmail(userFullInfo.getUser().getEmail(),userFullInfo.getUser().getFullName(), link));


}

public void FormFilled(String email) {
	userRepository.checkFilledForm(email);
}

public Page<User> unverfiedUsers(Pageable pgePageable){
	
	return  userRepository.findAllUnverfiedUsers(pgePageable);
}

public List<User> verfiedUsers(){
	
	return userRepository.findAllVerfiedUsers();
}

public int allUsersNumber() {
List<User> l=	userRepository.findAll();
return l.size()-1;
}


public void unlockUser(Long id) {
	userRepository.unLockUser(id);
}

public List<User> totalUser(){
	return userRepository.findAll();
}

public String adminEmail(String email,String fullName,String link) {
    return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
            "\n" +
            "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
            "\n" +
            "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
            "    <tbody><tr>\n" +
            "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
            "        \n" +
            "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
            "          <tbody><tr>\n" +
            "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
            "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
            "                  <tbody><tr>\n" +
            "                    <td style=\"padding-left:10px\">\n" +
            "                  \n" +
            "                    </td>\n" +
            "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
            "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">New User just SignUp</span>\n" +
            "                    </td>\n" +
            "                  </tr>\n" +
            "                </tbody></table>\n" +
            "              </a>\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "        </tbody></table>\n" +
            "        \n" +
            "      </td>\n" +
            "    </tr>\n" +
            "  </tbody></table>\n" +
            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
            "    <tbody><tr>\n" +
            "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
            "      <td>\n" +
            "        \n" +
            "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
            "                  <tbody><tr>\n" +
            "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
            "                  </tr>\n" +
            "                </tbody></table>\n" +
            "        \n" +
            "      </td>\n" +
            "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
            "    </tr>\n" +
            "  </tbody></table>\n" +
            "\n" +
            "\n" +
            "\n" +
            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
            "    <tbody><tr>\n" +
            "      <td height=\"30\"><br></td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
            "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
            "        \n" +
            "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi  ADMIN, </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> New user "+fullName+" just Signed up with "+ email + " Please Check Admin Panel </p>"+ "<a href=\"" + link + "\"><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <p>See you soon</p>" +
            "        \n" +
            "      </td>\n" +
            "      <td width=\"10\" valign=\"middle\"><br></td>\n"  +
            "    </tr>\n" +
            "    <tr>\n" +
            "      <td height=\"30\"><br></td>\n" +
            "    </tr>\n" +
            "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
            "\n" +
            "</div></div>";

}

}
