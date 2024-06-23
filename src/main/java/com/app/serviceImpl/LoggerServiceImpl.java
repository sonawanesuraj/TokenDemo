package com.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.LoggerDto;
import com.app.entity.LoggerEntity;
import com.app.entity.UserEntity;
import com.app.repository.LoggerRepository;
import com.app.serviceInterface.LoggerInterface;
@Service
public class LoggerServiceImpl implements LoggerInterface {

	@Autowired
	private LoggerRepository loggerRepository;

	@Override
	public LoggerEntity createLogger(LoggerDto dto, UserEntity entity) {
		LoggerEntity logger = new LoggerEntity();

		logger.setUserId(entity);
		logger.setToken(dto.getToken());
		logger.setExpireAt(dto.getExpireAt());
		loggerRepository.save(logger);
		return logger;
	}

	@Override
	public LoggerEntity getLoggerDetail(String token) {

		LoggerEntity loggerEntity;

		loggerEntity = this.loggerRepository.findByToken(token);
		return loggerEntity;
	}
	
	public LoggerEntity findByUser(UserEntity user) {
        return loggerRepository.findByUserId(user);
    }

    public void updateLogger(LoggerEntity logger) {
        loggerRepository.save(logger);
    }
	

}
