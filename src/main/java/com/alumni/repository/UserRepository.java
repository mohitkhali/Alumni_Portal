package com.alumni.repository;

import com.alumni.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.locked = TRUE WHERE a.UId = ?1")
    int unLockUser(Long id);

    
    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.filledForm = TRUE WHERE a.email = ?1")
    int checkFilledForm(String email);
    
    
    @Query("SELECT a FROM User a where a.locked=0") 
    public Page<User> findAllUnverfiedUsers(Pageable pageable);
    
    @Query("SELECT a FROM User a where a.locked=1") 
    public Page<User> findAllverfiedUsers(Pageable pageable);
    
    @Query("SELECT a FROM User a where a.locked=0") 
    public List<User> findAllUnverfiedUsers();
    
    @Query("SELECT a FROM User a where a.locked=1") 
    List<User> findAllVerfiedUsers();
  
    @Query("SELECT  email FROM User where locked=1") 
    List<String> findAllEmails();
    
    public User findByresetPasswordToken(String token);
    
   
   @Query("Select u from User  u where u.fullName like %:fullName% and u.filledForm=1 and u.locked=1")
   List<User> findByFullNameContaining( @Param("fullName")String fullName);
    
    
}
