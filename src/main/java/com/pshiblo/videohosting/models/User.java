package com.pshiblo.videohosting.models;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 *     id              serial              PRIMARY KEY,
 *     name            varchar(100)        NOT NULL,
 *     password_hash   varchar(255)        NOT NULL,
 *     token           varchar(255),
 *     email           varchar(100)        NOT NULL,
 *     img             varchar(255)        NOT NULL DEFAULT 'default.png',
 *     created         timestamp           NOT NULL DEFAULT NOW(),
 *     updated         timestamp           NOT NULL DEFAULT NOW()
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

}
