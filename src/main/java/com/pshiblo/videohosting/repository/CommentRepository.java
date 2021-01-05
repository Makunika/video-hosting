package com.pshiblo.videohosting.repository;

import com.pshiblo.videohosting.models.Comment;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Максим Пшибло
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByVideo(Video video);
}
