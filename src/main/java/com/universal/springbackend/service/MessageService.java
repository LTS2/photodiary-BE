package com.universal.springbackend.service;

import com.universal.springbackend.entity.Message;
import com.universal.springbackend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

	@Autowired
	private MessageRepository messageRepository;

	public List<Message> getMessagesByRecipientId(Long recipientId) {
		return messageRepository.findByRecipientId(recipientId);
	}

	public Message save(Message message) {
		return messageRepository.save(message);
	}

	public Optional<Message> findById(Long id) {
		return messageRepository.findById(id);
	}

	public void delete(Long id) {
		messageRepository.deleteById(id);
	}
}
