package com.pshiblo.videohosting.repository;

import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.models.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Максим Пшибло
 */
@Repository
public interface VideoRepository extends PagingAndSortingRepository<Video, UUID> {
    Video findByVideo(String video);
    List<Video> findByName(String name);
    List<Video> findByUser(User user);
    Page<Video> findByIsPrivate(boolean isPrivate, Pageable pageable);
}
