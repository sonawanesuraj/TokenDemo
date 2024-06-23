package com.app.serviceInterface;

import java.util.ArrayList;
import java.util.List;

import com.app.dto.UserDto;
import com.app.entity.UserEntity;


public interface UserServiceInterface {
	
	public void addUser(UserDto userDto);
	
	 public List<UserEntity> getAllUsers();
	

}
