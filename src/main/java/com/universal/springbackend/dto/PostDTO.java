package com.universal.springbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PostDTO {
	private Long id;
	private String title;
	private String caption;
	private String image;
	private String keywords;
	private String original;
	private Date createdDate;
	private Long authorId;
}
