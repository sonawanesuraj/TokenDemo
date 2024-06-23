package com.app.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.configuration.JwtTokenUtil;
import com.app.configuration.PasswordValidator;
import com.app.dto.AuthResponceDto;
import com.app.dto.ErrorResponseDto;
import com.app.dto.JwtRequestDto;
import com.app.dto.LoggerDto;
import com.app.dto.SuccessResponseDto;
import com.app.dto.UserDto;
import com.app.entity.LoggerEntity;
import com.app.entity.UserEntity;
import com.app.repository.UserRepository;
import com.app.serviceImpl.AuthServiceImpl;
import com.app.serviceImpl.LoggerServiceImpl;
import com.app.serviceImpl.UserServiceImpl;
import com.app.serviceInterface.UserServiceInterface;

@RestController
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserServiceInterface userServiceInterface;
	
	@Autowired
	private AuthServiceImpl authServiceImpl;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private LoggerServiceImpl loggerServiceImpl;

	
	@PostMapping("/register")
	public ResponseEntity<?>  registerUser(@RequestBody UserDto userDto)throws Exception,DataIntegrityViolationException {
		String email = userDto.getEmail();
		String password = userDto.getPassword();

		if (PasswordValidator.isValidforEmail(email)) {

			if (PasswordValidator.isValid(password)) {

				UserEntity databaseName = userRepository.findByEmailContainingIgnoreCase(email);
				if (databaseName == null) {
					userServiceInterface.addUser(userDto);
					return new ResponseEntity<>(new SuccessResponseDto("User Created", "userCreated", "data added"),
							HttpStatus.CREATED);

				} else {
					return new ResponseEntity<>(
							new ErrorResponseDto("User Email Id Already Exist", "userEmailIdAlreadyExist"),
							HttpStatus.BAD_REQUEST);
				}
			} else {

				return ResponseEntity.ok(new ErrorResponseDto(
						"Password should have Minimum 8 and maximum 50 characters, at least one uppercase letter, one lowercase letter, one number and one special character and No White Spaces",
						"Password validation not done"));
			}

		} else {
			return new ResponseEntity<>(new ErrorResponseDto("please check Email is not valid ", "Enter valid email"),
					HttpStatus.BAD_REQUEST);
		}

	}


@PostMapping("/login")
public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequestDto createAuthenticationToken) {
    try {
        // Debugging: Log the email being used for authentication

        UserEntity user = userRepository.findByEmailContainingIgnoreCase(createAuthenticationToken.getEmail());

        if (user == null) {
            // User not found, respond with error
            return ResponseEntity.ok(new ErrorResponseDto("Invalid email or Password", "Please enter valid email or password"));
        }

        // Debugging: Log user retrieval success

        // Log the entire user object to ensure it has the id

        if (authServiceImpl.comparePassword(createAuthenticationToken.getPassword(), user.getPassword())) {
            // Debugging: Log password match success

            final UserDetails userDetails = authServiceImpl.loadUserByUsername(createAuthenticationToken.getEmail());
            final String token = jwtTokenUtil.generateToken(userDetails);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, 5);
            Date expireAt = calendar.getTime();

            LoggerEntity loggerEntity = loggerServiceImpl.findByUser(user);
            if (loggerEntity == null) {
                // No existing logger entry, create a new one
                LoggerDto loggerDto = new LoggerDto();
                loggerDto.setToken(token);
                loggerDto.setExpireAt(expireAt);
                loggerServiceImpl.createLogger(loggerDto, user);
            } else {
                // Existing logger entry found, update it
                loggerEntity.setToken(token);
                loggerEntity.setExpireAt(expireAt);
                loggerServiceImpl.updateLogger(loggerEntity);
            }

            // Debugging: Log the user ID being sent in the response

            return ResponseEntity.ok(new SuccessResponseDto("Login Successful", "token",
                    new AuthResponceDto(token, user.getEmail(), user.getName(), user.getId())));
        } else {
            // Debugging: Log password mismatch
            System.out.println("Password mismatch for user: " + user.getEmail());
            return ResponseEntity.ok(new ErrorResponseDto("Invalid password", "Please enter valid password"));
        }
    } catch (Exception e) {
        // Debugging: Log the exception
        e.printStackTrace();
        return ResponseEntity.ok(new ErrorResponseDto("Invalid email or Password", "Please enter valid email or password"));
    }
}


@GetMapping("/getAllUsers")
public List<UserEntity>getAllUsers(){
	return userServiceInterface.getAllUsers();
	
}
}




	

	
	

	
	
	


