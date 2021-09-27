package com.github.leozin.repository;

import com.github.leozin.User;

import java.util.List;

public interface UserRepository {

    void save(User user);

    User findUserByEmail(String email);

    boolean exists(String email);

    boolean passwordMatches(User user);

    List<User> findAll();

    void update(User user);
}
