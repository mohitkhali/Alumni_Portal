package com.alumni.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class ContactUs {
	
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
  private  Long id;	
	
  private  String fullName;
  
  private  String email;
  
  private  String subject;
  
  @Size(max = 20)
  private  String  message;

public ContactUs( String fullName, String email, String subject, String message) {
	super();
	this.fullName = fullName;
	this.email = email;
	this.subject = subject;
	this.message = message;
}
  

  
}
