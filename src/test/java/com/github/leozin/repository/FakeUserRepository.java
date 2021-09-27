package com.github.leozin.repository;

import com.github.leozin.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeUserRepository implements UserRepository {

    private final Map<String, User> inMemory = new HashMap<>();

    @Override
    public void save(User user) {
        inMemory.put(user.getEmail(), user);
    }

    @Override
    public User findUserByEmail(String email) {
        return inMemory.get(email);
    }

    @Override
    public boolean exists(String email) {
        return inMemory.containsKey(email);
    }

    @Override
    public boolean passwordMatches(User user) {
        return user.getPassword().equals(inMemory.get(user.getEmail()).getPassword());
    }

    @Override
    public List<User> findAll() {
        return inMemory.keySet().stream().map(k -> inMemory.get(k)).collect(Collectors.toList());
    }

    @Override
    public void update(User user) {
        save(user);
    }

    public void reset() {
        inMemory.clear();
    }
}
