package com.universal.springbackend.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String caption;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Lob
    @Column(nullable = true)
    private String image;

    @Column(nullable = true)
    private String original;

    @Column(nullable = true)
    private String keywords;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
}
