package com.springboot_assignment_project.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

	private final EmailConfigProperties emailConfigProperties;

	public EmailConfig(EmailConfigProperties emailConfigProperties) {
		this.emailConfigProperties = emailConfigProperties;
	}

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(emailConfigProperties.getHost());
		mailSender.setPort(emailConfigProperties.getPort());
		mailSender.setUsername(emailConfigProperties.getUsername());
		mailSender.setPassword(emailConfigProperties.getPassword());

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		mailSender.setJavaMailProperties(properties);

		return mailSender;
	}
}
