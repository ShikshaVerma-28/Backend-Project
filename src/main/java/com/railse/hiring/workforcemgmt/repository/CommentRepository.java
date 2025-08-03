package com.railse.hiring.workforcemgmt.repository;

import com.railse.hiring.workforcemgmt.model.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(Long id);
    Comment save(Comment comment);
    List<Comment> findByTaskId(Long taskId);
}