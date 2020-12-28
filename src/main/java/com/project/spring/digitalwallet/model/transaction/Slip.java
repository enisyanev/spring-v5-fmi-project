package com.project.spring.digitalwallet.model.transaction;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
