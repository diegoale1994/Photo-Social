package com.rexsoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rexsoft.models.Comment;

public interface CommentRepo extends JpaRepository<Comment, Long> {

}
