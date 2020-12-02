package com.pshiblo.videohosting.models;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "password_hash")
    private String passwordHash;

    private String token;

    private String email;

    private String img;

}
