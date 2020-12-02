package com.pshiblo.videohosting.models;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

/**
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

    private String video;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
