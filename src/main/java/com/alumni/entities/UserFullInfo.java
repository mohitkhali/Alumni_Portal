package com.alumni.entities;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.lang.Nullable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class UserFullInfo {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long id;
	
	private String fatherName;
	
	private String motherName;
	
	private String gender;
	
	private Long rollNo;
	
	private Date passingYear;
	
	private String profileImage;
	
	private String permanentAddress;
	
	private String presentAddress;
	
	private String city;
	
	private String state;
	
	private String currentPos;
	
	private Long phoneNumber;
	
	private String jobProfile;
	
	private Date dob;
	
	private String jobLocation;
	
	private String companyName;
	
	private String skills;
	
	private String bio;
	
	@OneToOne
	@Cascade(CascadeType.DELETE)
	@JoinColumn(name = "user_id",nullable = true)
	private User user;

	public UserFullInfo(String fatherName, String motherName, String gender, Long rollNo, Date passingYear,
			String profileImage, String permanentAddress, String presentAddress, String city, String state,
			String currentPos, String jobProfile, String jobLocation, String companyName, String skills, String bio,Date dob,Long phoneNumber,
			User user) {
		super();
		this.fatherName = fatherName;
		this.motherName = motherName;
		this.gender = gender;
		this.rollNo = rollNo;
		this.passingYear = passingYear;
		this.profileImage = profileImage;
		this.permanentAddress = permanentAddress;
		this.presentAddress = presentAddress;
		this.city = city;
		this.dob=dob;
		this.phoneNumber=phoneNumber;
		this.state = state;
		this.currentPos = currentPos;
		this.jobProfile = jobProfile;
		this.jobLocation = jobLocation;
		this.companyName = companyName;
		this.skills = skills;
		this.bio = bio;
		this.user = user;
	}
	
	
	
	
}
