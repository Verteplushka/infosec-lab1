package com.InformationSecurity.Lab1.controller;

import com.InformationSecurity.Lab1.dto.ErrorResponse;
import com.InformationSecurity.Lab1.dto.LoginRequest;
import com.InformationSecurity.Lab1.dto.LoginResponse;
import com.InformationSecurity.Lab1.entity.User;
import com.InformationSecurity.Lab1.exception.AuthException;
import com.InformationSecurity.Lab1.repository.UserRepository;
import com.InformationSecurity.Lab1.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repo;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository repo, JwtService jwtService, PasswordEncoder encoder) {
        this.repo = repo;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody LoginRequest request) {
        if (repo.findByUsername(request.username()).isPresent()) {
            throw new AuthException("User already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(encoder.encode(request.password()));

        repo.save(user);
        String token = jwtService.generateToken(user.getUsername());
        return new LoginResponse(token);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        User user = repo.findByUsername(request.username())
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new AuthException("Invalid password");
        }

        String token = jwtService.generateToken(user.getUsername());
        return new LoginResponse(token);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
