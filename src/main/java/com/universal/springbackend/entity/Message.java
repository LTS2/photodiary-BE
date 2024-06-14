package com.universal.springbackend.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 쪽지 내용
    @Column(nullable = false)
    private String content;

    // 보낸 사람
    private String user;

    // 보낸 시간
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendAt;
}

