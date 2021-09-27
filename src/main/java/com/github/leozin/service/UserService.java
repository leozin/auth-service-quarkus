package com.github.leozin.service;

import com.github.leozin.User;
import com.github.leozin.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class UserService {

    private final UserRepository repository;
    private final HashingService encryptionService;

    @Inject
    public UserService(UserRepository repository, HashingService encryptionService) {
        this.repository = repository;
        this.encryptionService = encryptionService;
    }

    public void createNewUser(User user) {
        if (repository.exists(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail());
        }
        String encryptedPassword = encryptionService.encryptString(user.getPassword());
        user.setPassword(encryptedPassword);
        repository.save(user);
    }

    public void verifyCredentials(User user) {
        String encryptedPassword = encryptionService.encryptString(user.getPassword());
        user.setPassword(encryptedPassword);
        if (!repository.passwordMatches(user)) {
            throw new InvalidCredentialsException();
        }
    }

    public List<User> retrieveAllUsers() {
        return repository.findAll();
    }

    public void updateUser(User user) {
        repository.update(user);
    }
}
