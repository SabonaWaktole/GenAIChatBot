package edu.astu.chatbot.chat.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageReqDto {

    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @NotEmpty(message = "User question is required and must not be empty")
    private String userQuestion;
}

