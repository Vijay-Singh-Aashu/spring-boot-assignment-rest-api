package com.springboot_assignment_project.service;

import com.springboot_assignment_project.dto.UserDto;
import com.springboot_assignment_project.model.User;

public interface UserService {
	
	void registerNewUser(UserDto userDto);

	User verifyUserEmail(String token);

	void requestPasswordReset(String email);

	void resetPassword(String token, String newPassword);
}
