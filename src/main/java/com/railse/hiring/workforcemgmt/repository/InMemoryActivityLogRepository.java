package com.railse.hiring.workforcemgmt.repository;

import com.railse.hiring.workforcemgmt.model.ActivityLog;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryActivityLogRepository implements ActivityLogRepository {
    private final Map<Long, ActivityLog> activityLogStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public Optional<ActivityLog> findById(Long id) {
        return Optional.ofNullable(activityLogStore.get(id));
    }

    @Override
    public ActivityLog save(ActivityLog log) {
        if (log.getId() == null) {
            log.setId(idCounter.incrementAndGet());
        }
        activityLogStore.put(log.getId(), log);
        return log;
    }

    @Override
    public List<ActivityLog> findByTaskId(Long taskId) {
        return activityLogStore.values().stream()
                .filter(log -> log.getTaskId().equals(taskId))
                .sorted(Comparator.comparing(ActivityLog::getTimestamp)) // Sort chronologically
                .collect(Collectors.toList());
    }
}