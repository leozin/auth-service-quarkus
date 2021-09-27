package com.github.leozin.repository;

import com.github.leozin.User;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class JPAUserRepository implements UserRepository {

    @Override
    @Transactional
    public void save(User user) {
        UserEntity entity = new UserEntity();
        entity.email = user.getEmail();
        entity.password = user.getPassword();
        entity.firstName = user.getFirstName();
        entity.lastName = user.getLastName();

        entity.persistAndFlush();
    }

    @Override
    public User findUserByEmail(String email) {
        UserEntity result = UserEntity.findById(email);
        if (result != null) {
            return new User(result.email, result.password, result.firstName, result.lastName);
        }
        return null;
    }

    @Override
    public boolean exists(String email) {
        return UserEntity.count("email", email) > 0L;
    }

    @Override
    public boolean passwordMatches(User user) {
        return UserEntity.count(
                "email = ?1 and password = ?2",
                user.getEmail(), user.getPassword()) > 0;
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> result = UserEntity.listAll();
        return result.stream().map(
                        o -> new User(o.email, null, o.firstName, o.lastName))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void update(User user) {
        UserEntity entity = UserEntity.findById(user.getEmail());
        entity.firstName = user.getFirstName();
        entity.lastName = user.getLastName();

        entity.persist();
    }
}
