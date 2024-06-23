package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.LoggerEntity;
import com.app.entity.UserEntity;

public interface LoggerRepository  extends JpaRepository<LoggerEntity, Long>{
	
	LoggerEntity findByToken(String token);
	 LoggerEntity findByUserId(UserEntity user);


}
