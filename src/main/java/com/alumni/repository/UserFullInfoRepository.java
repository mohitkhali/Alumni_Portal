package com.alumni.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alumni.entities.User;
import com.alumni.entities.UserFullInfo;

public interface UserFullInfoRepository extends JpaRepository<UserFullInfo, Long> {
   List<UserFullInfo> findByUser(User user);

	@Query("select  u from UserFullInfo u where u.user=?1")
	UserFullInfo findByUserId(User user);
	
	
  
}
