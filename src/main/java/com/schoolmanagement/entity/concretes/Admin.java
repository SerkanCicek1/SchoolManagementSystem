package com.schoolmanagement.entity.concretes;

import com.schoolmanagement.entity.abstracts.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Admin extends User {

    private boolean built_in;
}