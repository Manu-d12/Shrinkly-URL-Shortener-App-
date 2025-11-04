package com.url.shortener.repositories;

import com.url.shortener.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {
    public Optional<User> findByUsername(String username);
}
