package com.pshiblo.videohosting.repository;

import com.pshiblo.videohosting.models.Mark;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Максим Пшибло
 */
public interface MarkRepository extends JpaRepository<Mark, Integer> {
    Mark findByUserAndVideo(User user, Video video);
    long countByVideoAndMark(Video video, Integer mark);
}
