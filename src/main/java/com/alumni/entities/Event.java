package com.alumni.entities;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Event {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long evid;
	
	private String eventName;
	
	private String venue;
	
	private Date dateOfEvent;
	
	private String startTime;

	private String endTime;
	
	private String orginizedBy;
	
	private String orgnizersContact;
	
	
	@Size(max = 500)
	private String aboutEvent;
	
	private String banner;

	public Event(String eventName, String venue, Date dateOfEvent, String startTime, String endTime,
			String orginizedBy, String orgnizersContact, String aboutEvent, String banner) {
		super();
		
		this.eventName = eventName;
		this.venue = venue;
		this.dateOfEvent = dateOfEvent;
		this.startTime = startTime;
		this.endTime = endTime;
		this.orginizedBy = orginizedBy;
		this.orgnizersContact = orgnizersContact;
		this.aboutEvent = aboutEvent;
		this.banner = banner;
	}
	
	
	
	
	
}
