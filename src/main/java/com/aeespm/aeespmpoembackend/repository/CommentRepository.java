package com.aeespm.aeespmpoembackend.repository;

import com.aeespm.aeespmpoembackend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.poem WHERE c.poem.id = :poemId ORDER BY c.createdAt DESC")
    List<Comment> findByPoemIdOrderByCreatedAtDesc(@Param("poemId") Long poemId);
    
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.poem ORDER BY c.createdAt DESC")
    Page<Comment> findAllWithPoem(Pageable pageable);
}
