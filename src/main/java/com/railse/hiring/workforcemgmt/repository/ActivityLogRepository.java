package com.railse.hiring.workforcemgmt.repository;

import com.railse.hiring.workforcemgmt.model.ActivityLog;
import java.util.List;
import java.util.Optional;

public interface ActivityLogRepository {
    Optional<ActivityLog> findById(Long id);
    ActivityLog save(ActivityLog log);
    List<ActivityLog> findByTaskId(Long taskId);
}