package com.pshiblo.videohosting.repository;

import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.models.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Максим Пшибло
 */
@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {
    Video findByVideo(String video);
    List<Video> findByName(String name);
    List<Video> findByUser(User user);
    List<Video> findByUserAndIsPrivate(User user, boolean isPrivate);
    Page<Video> findByIsPrivate(boolean isPrivate, Pageable pageable);
    Page<Video> findByIsPrivateAndNameContainsIgnoreCase(boolean isPrivate, String name, Pageable pageable);
}
