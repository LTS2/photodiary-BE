package com.universal.springbackend.service;

import com.universal.springbackend.entity.Message;
import com.universal.springbackend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

	@Autowired
	private MessageRepository messageRepository;


	public List<Message> getAllMessages() {
		return messageRepository.findAll();
	}

	public Message save(Message msg) {
		return messageRepository.save(msg);
	}
}
