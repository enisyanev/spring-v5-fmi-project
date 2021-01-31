package com.project.spring.digitalwallet.dao;

import com.project.spring.digitalwallet.model.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    User deleteByUsername(String username);

    User getUserByEmail(String email);
}
