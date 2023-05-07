package com.chatservice.repo;

import com.chatservice.model.Chat;
import com.chatservice.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message,Long> {
    @Query(value = "SELECT m FROM message m WHERE m.chat=:chatId ORDER BY m.creationTime ASC")
    List<Message> findByChatId(@Param("chatId") Chat id, Pageable pageable);
}
