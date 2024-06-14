package com.universal.springbackend.service;

import com.universal.springbackend.entity.User;
import com.universal.springbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User save(User user) {
		return userRepository.save(user);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public boolean usernameExists(String username) {
		return userRepository.findByUsername(username).isPresent();
	}
}
