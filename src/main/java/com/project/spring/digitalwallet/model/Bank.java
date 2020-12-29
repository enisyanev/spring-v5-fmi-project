package com.project.spring.digitalwallet.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Bank")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String iban;
    @NotNull
    private String swift;
    @NotNull
    private String country;
    @NotNull
    private long walletId;
}
