package com.universal.springbackend.controller;

import com.universal.springbackend.entity.Message;
import com.universal.springbackend.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/message")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@GetMapping("/")
	public List<Message> getAllMessages() {
		return messageService.getAllMessages();
	}

	@PostMapping("/")
	public ResponseEntity<Message> saveMessage(@RequestBody Message msg) {
		log.info(">>>>> MessageController.saveMessage.executed()");
		msg.setSendAt(new Date());
		Message savedMessage = messageService.save(msg);
		return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
	}
}
