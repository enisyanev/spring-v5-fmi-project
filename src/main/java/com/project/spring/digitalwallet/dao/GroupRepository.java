package com.project.spring.digitalwallet.dao;


import com.project.spring.digitalwallet.model.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByGroupName(String groupName);

    //Optional<Card> findByIdAndWalletId(long id, long walletId);
}
