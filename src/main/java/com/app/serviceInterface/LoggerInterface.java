package com.app.serviceInterface;

import com.app.dto.LoggerDto;
import com.app.entity.LoggerEntity;
import com.app.entity.UserEntity;

public interface LoggerInterface  {
	
	LoggerEntity createLogger(LoggerDto dto, UserEntity entity);

	LoggerEntity getLoggerDetail(String token);

}
