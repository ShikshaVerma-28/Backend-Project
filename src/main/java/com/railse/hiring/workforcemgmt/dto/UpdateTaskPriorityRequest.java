package com.railse.hiring.workforcemgmt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import com.railse.hiring.workforcemgmt.common.model.enums.Priority;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateTaskPriorityRequest {
    @NotNull
    private Long taskId;
    @NotNull
    private Priority newPriority;

    public Priority getNewPriority;
}