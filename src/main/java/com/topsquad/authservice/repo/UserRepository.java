package com.topsquad.authservice.repo;

import com.topsquad.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsernameOrEmail(String username, String email);

    Boolean existsByUsernameOrEmail(String username, String email);

}

