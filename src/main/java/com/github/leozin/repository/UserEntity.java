package com.github.leozin.repository;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class UserEntity extends PanacheEntityBase {

    @Id
    public String email;
    public String password;

    public String firstName;
    public String lastName;
    public Date createdAt;
    public Date updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();
    }
}
