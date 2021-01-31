package com.project.spring.digitalwallet.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.spring.digitalwallet.model.group.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByGroupName(String groupName);

    //Optional<Card> findByIdAndWalletId(long id, long walletId);
}