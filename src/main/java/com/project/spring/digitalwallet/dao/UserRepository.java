package com.project.spring.digitalwallet.dao;

import com.project.spring.digitalwallet.model.user.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);

    int countByUsername(String username);

    List<User> findByWalletId(long walletId);
}
