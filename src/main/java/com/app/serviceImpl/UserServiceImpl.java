package com.app.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.app.configuration.JwtTokenUtil;
import com.app.dto.AuthResponceDto;
import com.app.dto.JwtRequestDto;
import com.app.dto.LoggerDto;
import com.app.dto.SuccessResponseDto;
import com.app.dto.UserDto;
import com.app.entity.UserEntity;
import com.app.repository.UserRepository;
import com.app.serviceInterface.UserServiceInterface;

@Service
public class UserServiceImpl implements UserServiceInterface {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository; 
	
	

	@Override
	public void addUser(UserDto userDto) {
		UserEntity userEntity = new UserEntity();
		userEntity.setName(userDto.getName());
		userEntity.setEmail(userDto.getEmail());
		userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userRepository.save(userEntity);
		
			
		
	}
	
	@Override
	public List<UserEntity> getAllUsers() {
		System.err.println("ServiceImple GetAllUsers****");

		
		return userRepository.findAll();
	}
	
	
	
	
	
	
}


