package edu.astu.chatbot.chat;

import edu.astu.chatbot.chat.dto.MessageReqDto;
import edu.astu.chatbot.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final edu.astu.chatbot.chat.ChatService chatService;

    // Start a new chat session
    @PostMapping("/sessions")
    public ResponseEntity<ApiResponse<ChatSession>> startNewChat() {
        ChatSession chatSession = chatService.startNewChat();
        return ApiResponse.success("New chat session started successfully", chatSession);
    }

    // Get all chat sessions for a user
    @GetMapping("/sessions")
    public ResponseEntity<ApiResponse<List<ChatSession>>> getAllChatSessions() {
        List<ChatSession> sessions = chatService.getAllChatSessions();
        return ApiResponse.success("Chat sessions fetched successfully", sessions);
    }

    // Add a message to a chat session
    @PostMapping("/sessions/ask-question")
    public ResponseEntity<ApiResponse<Message>> addMessage(@RequestBody @Valid MessageReqDto messageReqDto) {
        Message message = chatService.addMessage(messageReqDto);
        return ApiResponse.success("Your question has been processed successfully", message);
    }

    // Get messages for a specific chat session
    @GetMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<ApiResponse<List<Message>>> getMessagesBySession(@PathVariable Long sessionId) {
        List<Message> messages = chatService.getMessagesBySession(sessionId);
        return ApiResponse.success("Messages fetched successfully", messages);
    }
}
