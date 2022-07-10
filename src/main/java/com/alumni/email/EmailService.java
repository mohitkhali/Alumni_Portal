package com.alumni.email;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

	private final static Logger LOGGER= LoggerFactory.getLogger(
			EmailService.class);
	@Override
	@Async
	public void send(String to, String email) {

			try {
				String host="smtp.gmail.com";
				Properties properties= System.getProperties();
				properties.put("mail.smtp.host", host);
				System.out.println("working");
				properties.put("mail.smtp.port", "465");
				properties.put("mail.smtp.ssl.enable", "true");
				properties.put("mail.smtp.auth", "true");
				Session session= Session.getInstance(properties, new Authenticator() {


							@Override
							protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
								return new javax.mail.PasswordAuthentication("your.email@gmail.com", "your.password")	;
							}
						}

				);

				session.setDebug(true);
				MimeMessage mesg= new MimeMessage(session);
				mesg.setFrom("your.email@gmail.com");
				mesg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				if(to=="admin@gmail.com") {
					mesg.setSubject("Alert");
				}
				else {
					mesg.setSubject("Hey");
				}
				mesg.setText(email,"UTF-8","html");
				Transport.send(mesg);


			}
			catch (Exception e) {
				throw new IllegalStateException("failed to send mail");
			}
		}
}
