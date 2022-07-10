package com.alumni.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alumni.entities.Jobs;

public interface JobRepository extends JpaRepository<Jobs, Long>{

}
