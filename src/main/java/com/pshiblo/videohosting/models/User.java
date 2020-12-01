package com.pshiblo.videohosting.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 *     id SERIAL PRIMARY KEY,
 *     name varchar(100) NOT NULL,
 *     password_hash varchar(255) NOT NULL,
 *     token varchar(255),
 *     email varchar(100) NOT NULL,
 *     img varchar(255) NOT NULL DEFAULT 'default.png'
 * @author Максим Пшибло
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity {

    private String name;

    @Column(name = "password_hash")
    private String passwordHash;

    private String token;

    private String email;

    private String img;

}
