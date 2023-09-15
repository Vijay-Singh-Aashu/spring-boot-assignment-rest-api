package com.springboot_assignment_project.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot_assignment_project.dto.UserDto;
import com.springboot_assignment_project.model.User;
import com.springboot_assignment_project.repository.UserRepository;

@Service
public class UserServiceImpl{
    
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JavaMailSender javaMailSender, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewUser(UserDto userDto) {
    	
        validateUserInput(userDto);

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered.");
        }

        // Create a new User entity
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); 
        user.setEnabled(false); 

        // Generate a verification token
        String verificationToken = generateVerificationToken();
        user.setVerificationToken(verificationToken);

        // Save the user to the database
        userRepository.save(user);

        // Send a verification email to the user
        sendVerificationEmail(user);
    }
    
    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
    
    private void sendVerificationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Email Verification");
        message.setText("To verify your email, click the following link: http://your-app-url/verify/" + user.getVerificationToken());
        javaMailSender.send(message);
    }

    private void validateUserInput(UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }
    }


    public User verifyUserEmail(String token) {
        // Find the user by the verification token
        Optional<User> optionalUser = userRepository.findByVerificationToken(token);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setVerificationToken(null); 
            user.setEnabled(true); 
            userRepository.save(user); 
            return user;
        } else {
            throw new RuntimeException("Invalid verification token.");
        }
    }

}

