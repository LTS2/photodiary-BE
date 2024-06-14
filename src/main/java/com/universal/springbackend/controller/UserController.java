package com.universal.springbackend.controller;

import com.universal.springbackend.entity.User;
import com.universal.springbackend.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@SessionAttributes("loginUser")
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

//	@GetMapping("/")
//	public List<User> getAllUsers(){
//	    log.info(">>>>> UserController.String.executed()");
//	    return userService.findAll();
//	}

	// 회원가입
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {
		if (userService.usernameExists(user.getUsername())) {
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}
		User savedUser = userService.save(user);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}

	// 아이디 중복검사
	@GetMapping("/check")
	public boolean checkUserName(@RequestParam String username) {
		log.info(">>>>> UserController.checkUserName.executed()");
		List<User> users = userService.findAll();
		return users.stream().noneMatch(user -> username.equals(user.getUsername()));  // null-safe comparison
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<User> loginUser(@RequestParam String username,
										  @RequestParam String password,
										  HttpSession session) {
		User loginUser = userService.findOne(username, password);
		if (loginUser != null) {
			session.setAttribute("loginUser", loginUser);
			return new ResponseEntity<>(loginUser, HttpStatus.OK);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	// 로그아웃
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpSession session) {
		session.removeAttribute("loginUser");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	// 전체 회원목록
	@GetMapping("/")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> userList = userService.findAll();
		if (!userList.isEmpty()) {
			return new ResponseEntity<>(userList, HttpStatus.OK);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}