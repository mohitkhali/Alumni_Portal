package com.alumni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alumni.entities.Messages;

@Repository
public interface MessageRepository extends JpaRepository<Messages, Long>{

}
