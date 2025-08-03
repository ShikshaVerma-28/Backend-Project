package com.railse.hiring.workforcemgmt.mapper;

import com.railse.hiring.workforcemgmt.dto.ActivityLogDto;
import com.railse.hiring.workforcemgmt.dto.CommentDto;
import com.railse.hiring.workforcemgmt.dto.TaskManagementDto;
import com.railse.hiring.workforcemgmt.model.ActivityLog;
import com.railse.hiring.workforcemgmt.model.Comment;
import com.railse.hiring.workforcemgmt.common.model.TaskManagement;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants; // Import for MappingConstants
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy; // Import for ReportingPolicy
// import org.mapstruct.factory.Mappers; // Usually not needed in Spring context

import java.util.List;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING, // Use type-safe constant for Spring integration
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, // Ignore null properties during mapping
        unmappedTargetPolicy = ReportingPolicy.IGNORE // Crucial: Ignore any properties in the source that don't exist in the target
)
public interface ITaskManagementMapper {

    // In a Spring application, you typically inject mappers using @Autowired,
    // so `ITaskManagementMapper INSTANCE = Mappers.getMapper(ITaskManagementMapper.class);`
    // is generally not used. I've commented it out as best practice for Spring.
    // ITaskManagementMapper INSTANCE = Mappers.getMapper(ITaskManagementMapper.class);

    // Maps a single ActivityLog model to its DTO
    ActivityLogDto activityLogToDto(ActivityLog activityLog);

    // Maps a list of ActivityLog models to a list of their DTOs
    List<ActivityLogDto> activityLogListToDtoList(List<ActivityLog> activityLogs);

    // Maps a single Comment model to its DTO
    CommentDto commentToDto(Comment comment);

    // Maps a list of Comment models to a list of their DTOs
    List<CommentDto> commentListToDtoList(List<Comment> comments);

    // Maps a TaskManagement model to TaskManagementDto
    // No @Mapping(ignore=true) needed for activityHistory/comments here,
    // because modelToDto is the first step, and the service will enrich these lists manually.
    // MapStruct will attempt to map other fields directly.
    TaskManagementDto modelToDto(TaskManagement model);

    // Maps a TaskManagementDto to TaskManagement model
    // The 'unmappedTargetPolicy = ReportingPolicy.IGNORE' at the @Mapper level
    // ensures that 'activityHistory' and 'comments' (which exist in DTO but not in Model)
    // are automatically ignored during this mapping. No explicit @Mapping(ignore=true) needed.
    TaskManagement dtoToModel(TaskManagementDto dto);

    // Maps a list of TaskManagement models to a list of TaskManagementDto
    // Similar to modelToDto, these lists will be enriched in the service.
    List<TaskManagementDto> modelListToDtoList(List<TaskManagement> models);

    // Add a mapping for DTO List to Model List if you need this conversion
    // List<TaskManagement> dtoListToModelList(List<TaskManagementDto> dtos);
}