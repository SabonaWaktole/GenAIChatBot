package edu.astu.chatbot.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    List<ChatSession> findByUserId(Long userId);

    Optional<ChatSession> findByUserIdAndSessionNameIgnoreCaseAndMessagesEmpty(Long userId, String sessionName);
}
