package com.railse.hiring.workforcemgmt.repository;

import com.railse.hiring.workforcemgmt.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryCommentRepository implements CommentRepository {
    private final Map<Long, Comment> commentStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(commentStore.get(id));
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            comment.setId(idCounter.incrementAndGet());
        }
        commentStore.put(comment.getId(), comment);
        return comment;
    }

    @Override
    public List<Comment> findByTaskId(Long taskId) {
        return commentStore.values().stream()
                .filter(comment -> comment.getTaskId().equals(taskId))
                .sorted(Comparator.comparing(Comment::getTimestamp)) // Sort chronologically
                .collect(Collectors.toList());
    }
}