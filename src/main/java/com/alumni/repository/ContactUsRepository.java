package com.alumni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alumni.entities.ContactUs;
import com.alumni.entities.User;
import com.alumni.entities.UserFullInfo;

public interface ContactUsRepository extends JpaRepository<ContactUs, Long> {

	
}
