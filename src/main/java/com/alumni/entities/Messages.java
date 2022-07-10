package com.alumni.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Messages {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long msgId;
	
	private String  subject;
	
	
	@Size(max = 10000)
	private String msg;

	@ManyToOne
	private User user;

	public Messages(String subject, String msg, User user) {
		super();
		this.subject = subject;
		this.msg = msg;
		this.user = user;
	}
	
	
	
}
