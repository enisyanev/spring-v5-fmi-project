package com.project.spring.digitalwallet.model.group;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.project.spring.digitalwallet.model.user.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "GroupFunding")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Group {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	private int targetMoney;
	private int currentMoney;
	private String groupDescription;
	private String groupName;
}
