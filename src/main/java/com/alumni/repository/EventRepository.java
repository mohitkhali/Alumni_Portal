package com.alumni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alumni.entities.Event;


@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

	
	
}
