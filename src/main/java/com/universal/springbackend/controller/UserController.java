package com.universal.springbackend.controller;

import com.universal.springbackend.entity.User;
import com.universal.springbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

//	@GetMapping("/")
//	public List<User> getAllUsers(){
//	    log.info(">>>>> UserController.String.executed()");
//	    return userService.findAll();
//	}

	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {
		if (userService.usernameExists(user.getUsername())) {
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}
		User savedUser = userService.save(user);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}

	@GetMapping("/")
	public boolean checkUserName(@RequestParam String username) {
		log.info(">>>>> UserController.checkUserName.executed()");
		List<User> users = userService.findAll();
		return users.stream().noneMatch(user -> username.equals(user.getUsername()));  // null-safe comparison
	}
}

