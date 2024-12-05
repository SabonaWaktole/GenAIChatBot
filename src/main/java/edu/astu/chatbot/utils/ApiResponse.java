package edu.astu.chatbot.utils;

import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
public class ApiResponse<T> {

    private String code;

    private String message;

    private T data;

    // Constructor for code and msg
    public ApiResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.data = null; // Initialize data to null
    }

    // Constructor for code and data
    public ApiResponse(String message, T data) {
        this.code = "0";
        this.message = message; // Initialize msg to null
        this.data = data;
    }

    // Constructor for message
    public ApiResponse(String message) {
        this.code = "0";
        this.message = message; // Initialize msg to null
        this.data = null;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<T>(message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message) {
        return ResponseEntity.ok(new ApiResponse<T>(message));
    }
}
