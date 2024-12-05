package edu.astu.chatbot.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.astu.chatbot.user.dto.UserRegistrationDto;
import edu.astu.chatbot.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Users>> register(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        Users user = userService.registerUser(userRegistrationDto);
        return ApiResponse.success("User registered successfully", user);
        
    }


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Users>> getMe() {
        Users user = userService.getCurrentlyLoggedInUser();
        return ApiResponse.success("User fetched successfully sabona", user);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Users>>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return ApiResponse.success("Users fetched successfully", users);
    }

    @DeleteMapping({"/{username}"})
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ApiResponse.success("User deleted successfully");
    }

}


