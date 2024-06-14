package com.universal.springbackend.dto;

import lombok.Data;

@Data
public class MessageDTO {
	private Long recipientId;
	private String content;
}
