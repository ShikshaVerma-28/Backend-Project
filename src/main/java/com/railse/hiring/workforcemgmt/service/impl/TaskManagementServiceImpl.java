package com.railse.hiring.workforcemgmt.service.impl;

import com.railse.hiring.workforcemgmt.common.exception.ResourceNotFoundException;
import com.railse.hiring.workforcemgmt.common.model.enums.ActivityEventType;
import com.railse.hiring.workforcemgmt.common.model.enums.Priority;
import com.railse.hiring.workforcemgmt.common.model.enums.TaskStatus;
import com.railse.hiring.workforcemgmt.dto.TaskCreateRequest;
import com.railse.hiring.workforcemgmt.dto.TaskManagementDto;
import com.railse.hiring.workforcemgmt.dto.UpdateTaskRequest;
import com.railse.hiring.workforcemgmt.mapper.ITaskManagementMapper;
import com.railse.hiring.workforcemgmt.model.ActivityLog;
import com.railse.hiring.workforcemgmt.model.Comment;
import com.railse.hiring.workforcemgmt.common.model.TaskManagement;
import com.railse.hiring.workforcemgmt.repository.InMemoryActivityLogRepository;
import com.railse.hiring.workforcemgmt.repository.InMemoryCommentRepository;
import com.railse.hiring.workforcemgmt.repository.InMemoryTaskRepository;
import com.railse.hiring.workforcemgmt.service.TaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {

    private final InMemoryTaskRepository taskRepository;
    private final InMemoryActivityLogRepository activityLogRepository;
    private final InMemoryCommentRepository commentRepository;
    private final ITaskManagementMapper taskManagementMapper;

    @Autowired
    public TaskManagementServiceImpl(InMemoryTaskRepository taskRepository,
                                     InMemoryActivityLogRepository activityLogRepository,
                                     InMemoryCommentRepository commentRepository,
                                     ITaskManagementMapper taskManagementMapper) {
        this.taskRepository = taskRepository;
        this.activityLogRepository = activityLogRepository;
        this.commentRepository = commentRepository;
        this.taskManagementMapper = taskManagementMapper;
    }

    @Override
    @Transactional
    public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
        List<TaskManagement> createdTasks = createRequest.getRequests().stream()
                .map(requestItem -> {
                    TaskManagement task = new TaskManagement();
                    task.setReferenceId(requestItem.getReferenceId());
                    task.setReferenceType(requestItem.getReferenceType());
                    task.setTask(requestItem.getTask());
                    task.setAssigneeId(requestItem.getAssigneeId());
                    task.setPriority(requestItem.getPriority());
                    task.setDescription(requestItem.getDescription());
                    task.setTaskDeadlineTime(requestItem.getTaskDeadlineTime());
                    task.setStatus(TaskStatus.ASSIGNED);

                    TaskManagement savedTask = taskRepository.save(task);

                    // Log activity
                    activityLogRepository.save(new ActivityLog(
                            null,
                            savedTask.getId(),
                            Instant.now().toEpochMilli(),
                            ActivityEventType.TASK_CREATED,
                            "Task '" + savedTask.getTask() + "' created and assigned to " + savedTask.getAssigneeId(),
                            "SYSTEM" // Or actual user ID from context
                    ));
                    return savedTask;
                })
                .collect(Collectors.toList());

        return createdTasks.stream()
                .map(this::mapModelToDtoWithRelations)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskManagementDto addCommentToTask(Long taskId, String userId, String commentText) {
        TaskManagement task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        Comment comment = new Comment(null, taskId, userId, commentText, Instant.now().toEpochMilli());
        commentRepository.save(comment);

        // Log activity
        activityLogRepository.save(new ActivityLog(
                null,
                taskId,
                Instant.now().toEpochMilli(),
                ActivityEventType.COMMENT_ADDED,
                "Comment added by " + userId + ": \"" + commentText + "\"",
                userId
        ));

        return mapModelToDtoWithRelations(task);
    }

    @Override
    @Transactional
    public TaskManagementDto updateTaskPriority(Long taskId, Priority newPriority, String userId) {
        TaskManagement task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        Priority oldPriority = task.getPriority();
        if (!oldPriority.equals(newPriority)) {
            task.setPriority(newPriority);
            task.setLastUpdatedTime(Instant.now().toEpochMilli());
            taskRepository.save(task);

            // Log activity
            activityLogRepository.save(new ActivityLog(
                    null,
                    taskId,
                    Instant.now().toEpochMilli(),
                    ActivityEventType.TASK_PRIORITY_CHANGED,
                    "Priority changed from " + oldPriority + " to " + newPriority,
                    userId
            ));
        }
        return mapModelToDtoWithRelations(task);
    }

    @Override
    public List<TaskManagementDto> getTasksByPriority(Priority priority) {
        return taskRepository.findByPriority(priority).stream()
                .map(this::mapModelToDtoWithRelations)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskManagementDto updateTask(Long taskId, UpdateTaskRequest.RequestItem updateItem) {
        TaskManagement task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        StringBuilder changeDescription = new StringBuilder("Task updated: ");
        boolean changed = false;

        // Store old values for logging
        TaskStatus oldStatus = task.getStatus();
        String oldDescription = task.getDescription();
        Long oldAssigneeId = task.getAssigneeId();
        Long oldDeadlineTime = task.getTaskDeadlineTime();

        if (updateItem.getTaskStatus() != null && !updateItem.getTaskStatus().equals(task.getStatus())) {
            changeDescription.append("Status from ").append(oldStatus).append(" to ").append(updateItem.getTaskStatus()).append("; ");
            task.setStatus(updateItem.getTaskStatus());
            // Log status change as a specific event
            activityLogRepository.save(new ActivityLog(null, taskId, Instant.now().toEpochMilli(),
                    ActivityEventType.TASK_STATUS_CHANGED, "Status changed to " + updateItem.getTaskStatus(), "SYSTEM"));
            changed = true;
        }
        if (updateItem.getDescription() != null && !updateItem.getDescription().equals(task.getDescription())) {
            changeDescription.append("Description changed; ");
            task.setDescription(updateItem.getDescription());
            // Log description update as a generic task updated event
            activityLogRepository.save(new ActivityLog(null, taskId, Instant.now().toEpochMilli(),
                    ActivityEventType.TASK_UPDATED, "Description updated", "SYSTEM"));
            changed = true;
        }
        if (updateItem.getAssigneeId() != null && !updateItem.getAssigneeId().equals(task.getAssigneeId())) {
            changeDescription.append("Assignee from ").append(oldAssigneeId).append(" to ").append(updateItem.getAssigneeId()).append("; ");
            task.setAssigneeId(updateItem.getAssigneeId());
            // Log assignee change as a specific event
            activityLogRepository.save(new ActivityLog(null, taskId, Instant.now().toEpochMilli(),
                    ActivityEventType.TASK_ASSIGNED, "Assigned to " + updateItem.getAssigneeId(), "SYSTEM"));
            changed = true;
        }
        if (updateItem.getTaskDeadlineTime() != null && !updateItem.getTaskDeadlineTime().equals(task.getTaskDeadlineTime())) {
            changeDescription.append("Deadline updated; ");
            task.setTaskDeadlineTime(updateItem.getTaskDeadlineTime());
            // Log deadline update as a specific event
            activityLogRepository.save(new ActivityLog(null, taskId, Instant.now().toEpochMilli(),
                    ActivityEventType.TASK_DEADLINE_UPDATED,
                    "Deadline updated to " + Instant.ofEpochMilli(updateItem.getTaskDeadlineTime()).atZone(ZoneOffset.UTC).toLocalDate(), "SYSTEM"));
            changed = true;
        }

        if (changed) {
            task.setLastUpdatedTime(Instant.now().toEpochMilli());
            taskRepository.save(task);

            activityLogRepository.save(new ActivityLog(
                    null,
                    taskId,
                    Instant.now().toEpochMilli(),
                    ActivityEventType.TASK_UPDATED,
                    changeDescription.toString().trim(),
                    "SYSTEM"
            ));
        }

        return mapModelToDtoWithRelations(task);
    }

    @Override
    @Transactional
    public List<TaskManagementDto> updateMultipleTasks(UpdateTaskRequest updateRequest) {
        return updateRequest.getRequests().stream()
                .map(item -> updateTask(item.getTaskId(), item))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);
        activityLogRepository.save(new ActivityLog(
                null,
                id,
                Instant.now().toEpochMilli(),
                ActivityEventType.TASK_DELETED,
                "Task with ID " + id + " deleted.",
                "SYSTEM"
        ));
    }

    @Override
    public List<TaskManagementDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapModelToDtoWithRelations)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskManagementDto> getTasksByAssigneeId(Long assigneeId) {
        return taskRepository.findByAssigneeId(assigneeId).stream()
                .map(this::mapModelToDtoWithRelations)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskManagementDto> getTasksCreatedInDateRange(Long startDate, Long endDate) {
        return taskRepository.findTasksByDateRange(startDate, endDate).stream()
                .map(this::mapModelToDtoWithRelations)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskManagementDto> getTasksByAssigneeIds(List<Long> assigneeIds) {
        return taskRepository.findByAssigneeIds(assigneeIds).stream()
                .map(this::mapModelToDtoWithRelations)
                .collect(Collectors.toList());
    }

    private TaskManagementDto mapModelToDtoWithRelations(TaskManagement model) {
        TaskManagementDto dto = taskManagementMapper.modelToDto(model);
        dto.setActivityHistory(taskManagementMapper.activityLogListToDtoList(
                activityLogRepository.findByTaskId(model.getId())
        ));
        dto.setComments(taskManagementMapper.commentListToDtoList(
                commentRepository.findByTaskId(model.getId())
        ));
        return dto;
    }
}