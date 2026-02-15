package com.tourism.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tourism.dto.AuthResponseDTO;
import com.tourism.dto.LoginRequestDTO;
import com.tourism.dto.SignupRequestDTO;
import com.tourism.entity.User;
import com.tourism.repository.UserRepository;
import com.tourism.security.JwtUtil;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /* ================= SIGNUP ================= */

    public AuthResponseDTO signup(SignupRequestDTO request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
            passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);

        // Optional: auto-login after signup
        String token = jwtUtil.generateToken(savedUser.getEmail());

        return new AuthResponseDTO(
            token,
            savedUser.getEmail()
        );
    }

    /* ================= LOGIN ================= */

    public AuthResponseDTO login(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponseDTO(
            token,
            user.getEmail()
        );
    }
}
