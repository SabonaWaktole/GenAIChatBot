package edu.astu.chatbot.user;

import edu.astu.chatbot.common.exceptions.ResourceAlreadyExistException;
import edu.astu.chatbot.common.exceptions.ResourceNotFoundException;
import edu.astu.chatbot.common.exceptions.UnauthorizedException;
import edu.astu.chatbot.user.dto.UserRegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Users registerUser(UserRegistrationDto userRegistrationDto) {
        boolean exists = userRepository.existsByUsername(userRegistrationDto.getUsername());
        if (exists)
            throw new ResourceAlreadyExistException("User with username '" + userRegistrationDto.getUsername() + "' already exists");

        Users user = Users.builder()
                .fullName(userRegistrationDto.getFullName())
                .username(userRegistrationDto.getUsername())
                .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                .build();

        return userRepository.save(user);
    }

    //returns currently logged-in user
    public Users getCurrentlyLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new UnauthorizedException("Access denied. Please provide a valid authentication token.");

        String username = authentication.getName();
        return findByUsername(username);
    }


    //returns currently logged in user id
    public Long getCurrentlyLoggedInUserId() {
        return getCurrentlyLoggedInUser().getId();
    }

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username '" + username + "' does not exist"));
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String username) {
        Users user = findByUsername(username);
        userRepository.delete(user);
    }
}
