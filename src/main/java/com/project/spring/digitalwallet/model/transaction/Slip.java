package com.project.spring.digitalwallet.model.transaction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

/**
 * Table used as a sequence to generate incrementing IDs.
 */
@Entity
@Table(name = "SLIP_ID")
@Getter
public class Slip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
