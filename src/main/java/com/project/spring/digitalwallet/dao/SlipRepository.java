package com.project.spring.digitalwallet.dao;

import com.project.spring.digitalwallet.model.transaction.Slip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlipRepository extends JpaRepository<Slip, Long> {

}
