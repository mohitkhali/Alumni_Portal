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
public class Jobs {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long jId;
	
	private String companyName;
	
	private String companyWebsite;
	
	private String jobRole;
	
	private Long experience;
	
	private String location;
	
	private String requiredSkills;
	
	private Long salary;
	
	@Size(max = 1000)
	private String aboutJob;
	
	private String applyLink;
	
	
	@ManyToOne
	private User user;


	public Jobs(String companyName, String companyWebsite, String jobRole, Long experience, String location,
			String requiredSkills, Long salary, String aboutJob, String applyLink, User user) {
		super();
		this.companyName = companyName;
		this.companyWebsite = companyWebsite;
		this.jobRole = jobRole;
		this.experience = experience;
		this.location = location;
		this.requiredSkills = requiredSkills;
		this.salary = salary;
		this.aboutJob = aboutJob;
		this.applyLink = applyLink;
		this.user = user;
	}
	
}
