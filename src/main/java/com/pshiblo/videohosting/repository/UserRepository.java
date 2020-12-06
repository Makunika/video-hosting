package com.pshiblo.videohosting.repository;

import com.pshiblo.videohosting.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Максим Пшибло
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByName(String name);
}
