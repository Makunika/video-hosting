package com.pshiblo.videohosting.models;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

/**
 *     id              varchar(36)         PRIMARY KEY,
 *     name            varchar(100)        NOT NULL,
 *     about           text,
 *     video           varchar(255)        NOT NULL DEFAULT 'default.mp4',
 *     private         bool                NOT NULL DEFAULT false,
 *
 *
 *
 *     user_id         int                 NOT NULL,
 *     created         timestamp           NOT NULL DEFAULT NOW(),
 *     updated         timestamp           NOT NULL DEFAULT NOW(),
 * @author Максим Пшибло
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Videos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Video extends BaseEntity {

    @Id
    @GeneratedValue
    @Type(type="uuid-char")
    private UUID id;

    private String name;

    private String video;

    private String about;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
