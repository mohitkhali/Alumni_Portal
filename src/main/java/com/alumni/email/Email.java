package com.alumni.email;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Email {
	
	@Id
	private long id;
	
	private String message;

}
