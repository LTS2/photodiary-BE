package com.universal.springbackend.controller;

import com.universal.springbackend.dto.MessageDTO;
import com.universal.springbackend.entity.Message;
import com.universal.springbackend.entity.User;
import com.universal.springbackend.service.MessageService;
import com.universal.springbackend.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@SessionAttributes("loginUser")
@RequestMapping("/api/messages")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@PostMapping("/")
	public ResponseEntity<Message> sendMessage(@RequestBody MessageDTO messageDTO, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Optional<User> recipientOpt = userService.findById(messageDTO.getRecipientId());
		if (!recipientOpt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		User recipient = recipientOpt.get();

		Message message = new Message();
		message.setContent(messageDTO.getContent());
		message.setSender(loginUser);
		message.setRecipient(recipient);
		message.setSendAt(new Date());

		Message savedMessage = messageService.save(message);
		return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
	}

	@GetMapping("/received")
	public ResponseEntity<List<Message>> getReceivedMessages(HttpSession session) {
		log.info("getReceivedMessages 실행함");
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		List<Message> messages = messageService.getMessagesByRecipientId(loginUser.getId());
		return new ResponseEntity<>(messages, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMessage(@PathVariable Long id, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Optional<Message> messageOpt = messageService.findById(id);
		if (!messageOpt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Message message = messageOpt.get();
		if (!message.getRecipient().getId().equals(loginUser.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		messageService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
