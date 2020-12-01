package com.pshiblo.videohosting.repository;

import com.pshiblo.videohosting.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Максим Пшибло
 */
@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    Video findByVideo(String video);
}
