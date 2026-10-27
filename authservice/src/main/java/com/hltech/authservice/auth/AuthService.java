
package com.hltech.authservice.auth;

import com.hltech.authservice.user.AppUser;
import com.hltech.authservice.user.UserRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String fullName = request.fullName().trim();
        String email = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);
        String phone = request.phoneNumber().trim();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered"
            );
        }

        if (userRepository.existsByPhoneNumber(phone)) {
            throw new IllegalArgumentException(
                    "Mobile number is already registered"
            );
        }

        AppUser user = new AppUser(
                fullName,
                email,
                phone,
                passwordEncoder.encode(request.password())
        );

        try {
            user = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {
            // Unique constraints protect against concurrent registrations.
            throw new IllegalArgumentException(
                    "Email or mobile number is already registered"
            );
        }

        return toResponse("Registration successful", user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        String identifier = request.identifier().trim();
        AppUser user;

        if (identifier.contains("@")) {

            String email = identifier.toLowerCase(Locale.ROOT);

            user = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new BadCredentialsException(
                                    "Invalid login credentials"
                            )
                    );

        } else {

            user = userRepository.findByPhoneNumber(identifier)
                    .orElseThrow(() ->
                            new BadCredentialsException(
                                    "Invalid login credentials"
                            )
                    );
        }

        if (!user.isEnabled()
                || !passwordEncoder.matches(
                        request.password(),
                        user.getPasswordHash()
                )) {
            throw new BadCredentialsException(
                    "Invalid login credentials"
            );
        }

        return toResponse("Login successful", user);
    }

    private AuthResponse toResponse(
            String message,
            AppUser user
    ) {
        return new AuthResponse(
                message,
                jwtService.generateToken(user),
                "Bearer",
                jwtService.getExpirationSeconds(),
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
}
