package edu.astu.chatbot.chat;

import edu.astu.chatbot.chat.dto.MessageReqDto;
import edu.astu.chatbot.integration.GeminiAIChatbotAdapter;
import edu.astu.chatbot.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final GeminiAIChatbotAdapter geminiAIChatbotAdapter;

    // Start a new chat session
    @Transactional
    public ChatSession startNewChat() {
        Long userId = userService.getCurrentlyLoggedInUserId();
        String sessionName = "New Chat";
        Optional<ChatSession> existingSession = chatSessionRepository.findByUserIdAndSessionNameIgnoreCaseAndMessagesEmpty(userId, sessionName);

        //return existing session if messages are empty
        if (existingSession.isPresent()) {
            return existingSession.get();
        }

        // Create a new session
        ChatSession chatSession = new ChatSession();
        chatSession.setUserId(userId);
        chatSession.setSessionName(sessionName);
        return chatSessionRepository.save(chatSession);
    }

    // Get all chat sessions for a user
    public List<ChatSession> getAllChatSessions() {
        Long userId = userService.getCurrentlyLoggedInUserId();

        List<ChatSession> chatSessions = new ArrayList<>();
        chatSessionRepository.findByUserId(userId).forEach(chatSession -> {
            ChatSession session = new ChatSession();
            session.setId(chatSession.getId());
            session.setSessionName(chatSession.getSessionName());
            session.setCreatedAt(chatSession.getCreatedAt());
            session.setUpdatedAt(chatSession.getUpdatedAt());
            session.setMessages(null);
            chatSessions.add(session);

        });

        return chatSessions;
    }

    // Add a message to a chat session
    public Message addMessage(MessageReqDto messageReqDto) {
        Optional<ChatSession> optionalSession = chatSessionRepository.findById(messageReqDto.getSessionId());
        if (optionalSession.isEmpty()) {
            throw new IllegalArgumentException("Chat session not found");
        }

        ChatSession chatSession = optionalSession.get();

        Message message = new Message();
        message.setChatSession(chatSession);
        message.setUserRequest(messageReqDto.getUserQuestion());
        messageRepository.save(message);

        String botResponse = geminiAIChatbotAdapter.processRequest(messageReqDto.getUserQuestion());
        message.setBotResponse(botResponse);
        message = messageRepository.save(message);

        System.err.println("Size of messages: " + chatSession.getMessages().size());

        if (chatSession.getMessages().size() == 1) {
            System.err.println("Here for title");
            String chatTitle = createChatSessionTitle(message);
            chatSession.setSessionName(chatTitle);
            chatSessionRepository.save(chatSession);
        }

        return message;
    }


    private String createChatSessionTitle(Message message) {
        String initialUserRequest = message.getUserRequest();

        String requestForTitle = "Request: '" + initialUserRequest + "'";

        String request = requestForTitle + ". summarize this Request with 4 words only.";


        return geminiAIChatbotAdapter.processRequest(request);
    }

    // Get messages for a specific chat session
    public List<Message> getMessagesBySession(Long sessionId) {
        Long userId = userService.getCurrentlyLoggedInUserId();
        return messageRepository.findByChatSessionId(sessionId);
    }
}
