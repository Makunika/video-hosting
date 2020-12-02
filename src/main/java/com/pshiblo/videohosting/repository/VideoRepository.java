package com.pshiblo.videohosting.repository;

import com.pshiblo.videohosting.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Максим Пшибло
 */
@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {
    Video findByVideo(String video);
}
