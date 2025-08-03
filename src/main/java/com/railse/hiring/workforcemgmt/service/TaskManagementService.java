package com.railse.hiring.workforcemgmt.service;

import com.railse.hiring.workforcemgmt.common.model.enums.Priority;
import com.railse.hiring.workforcemgmt.dto.*;

import java.util.List;

public interface TaskManagementService {
    List<TaskManagementDto> createTasks(TaskCreateRequest createRequest);
    TaskManagementDto addCommentToTask(Long taskId, String userId, String commentText);
    TaskManagementDto updateTaskPriority(Long taskId, Priority newPriority, String userId);
    List<TaskManagementDto> getTasksByPriority(Priority priority);

    TaskManagementDto updateTask(Long taskId, UpdateTaskRequest.RequestItem updateItem);
//    List<TaskManagementDto> updateMultipleTasks(UpdateTaskRequest updateRequest);
//    void deleteTask(Long id);
//    List<TaskManagementDto> getAllTasks();
//    List<TaskManagementDto> getTasksByAssigneeId(Long assigneeId);
//    List<TaskManagementDto> getTasksCreatedInDateRange(Long startDate, Long endDate);
//    List<TaskManagementDto> getTasksByAssigneeIds(List<Long> assigneeIds);

    List<TaskManagementDto> updateTasks(UpdateTaskRequest request);

    TaskManagementDto findTaskById(Long id);

    String assignByReference(AssignByReferenceRequest request);

    List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request);
}