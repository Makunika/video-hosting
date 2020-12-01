package com.pshiblo.videohosting.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Максим Пшибло
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Videos")
@Data
public class Video extends BaseEntity {

    private String video;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
