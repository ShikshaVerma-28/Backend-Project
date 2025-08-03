package com.railse.hiring.workforcemgmt.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.railse.hiring.workforcemgmt.common.model.enums.Priority;
import com.railse.hiring.workforcemgmt.common.model.enums.TaskStatus;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import com.railse.hiring.workforcemgmt.common.model.enums.ReferenceType;


import com.railse.hiring.workforcemgmt.model.enums.Task;


import lombok.Data;
import java.util.List;

@Data

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class TaskManagementDto {

    private Long id;

    private Long referenceId;

    private ReferenceType referenceType;

    private Task task;

    private String description;

    private TaskStatus status;

    private Long assigneeId;

    private Long taskDeadlineTime;

    private Priority priority;
    private List<ActivityLogDto> activityHistory;
    private List<CommentDto> comments;
}