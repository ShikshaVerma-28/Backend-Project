package com.railse.hiring.workforcemgmt.model;

import com.railse.hiring.workforcemgmt.common.model.enums.ActivityEventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {
    private Long id;
    private Long taskId;
    private Long timestamp;
    private ActivityEventType eventType;
    private String description;
    private String userId;
}