package dev.bomboclat.service;

import dev.bomboclat.model.User;
import dev.bomboclat.util.JwtUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class AuthService {

    @Inject
    JwtUtil jwtUtil;

    /**
     * Authenticate a user with email and password
     *
     * @param email    The email to authenticate
     * @param password The password to authenticate
     * @return Optional containing the JWT token if authentication is successful, empty otherwise
     */
    @Inject
    UserService userService;

    public Optional<String> authenticate(String email, String password) {
        return userService.authenticate(email, password)
                .map(u -> jwtUtil.generateToken(u.email));
    }
}