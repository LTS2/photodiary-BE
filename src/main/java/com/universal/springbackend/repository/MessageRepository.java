package com.universal.springbackend.repository;

import com.universal.springbackend.entity.Message;
import com.universal.springbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}