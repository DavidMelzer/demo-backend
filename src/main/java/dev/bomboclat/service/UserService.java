package dev.bomboclat.service;

import dev.bomboclat.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class UserService {

    @Transactional
    public User register(String email, String password, String name) {
        if (User.find("email", email).firstResult() != null) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User();
        user.email = email;
        user.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        user.name = name;
        user.persist();
        return user;
    }

    public Optional<User> authenticate(String email, String password) {
        User user = User.find("email", email).firstResult();
        if (user != null && BCrypt.checkpw(password, user.passwordHash)) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
