package com.railse.hiring.workforcemgmt.mapper;

import com.railse.hiring.workforcemgmt.common.model.TaskManagement;
import com.railse.hiring.workforcemgmt.dto.ActivityLogDto;
import com.railse.hiring.workforcemgmt.dto.CommentDto;
import com.railse.hiring.workforcemgmt.dto.TaskManagementDto;
import com.railse.hiring.workforcemgmt.model.ActivityLog;
import com.railse.hiring.workforcemgmt.model.Comment;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-03T23:03:21+0530",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 17.0.16 (Oracle Corporation)"
)
@Component
public class ITaskManagementMapperImpl implements ITaskManagementMapper {

    @Override
    public ActivityLogDto activityLogToDto(ActivityLog activityLog) {
        if ( activityLog == null ) {
            return null;
        }

        ActivityLogDto activityLogDto = new ActivityLogDto();

        activityLogDto.setId( activityLog.getId() );
        activityLogDto.setTaskId( activityLog.getTaskId() );
        activityLogDto.setTimestamp( activityLog.getTimestamp() );
        activityLogDto.setEventType( activityLog.getEventType() );
        activityLogDto.setDescription( activityLog.getDescription() );
        activityLogDto.setUserId( activityLog.getUserId() );

        return activityLogDto;
    }

    @Override
    public List<ActivityLogDto> activityLogListToDtoList(List<ActivityLog> activityLogs) {
        if ( activityLogs == null ) {
            return null;
        }

        List<ActivityLogDto> list = new ArrayList<ActivityLogDto>( activityLogs.size() );
        for ( ActivityLog activityLog : activityLogs ) {
            list.add( activityLogToDto( activityLog ) );
        }

        return list;
    }

    @Override
    public CommentDto commentToDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDto commentDto = new CommentDto();

        commentDto.setId( comment.getId() );
        commentDto.setTaskId( comment.getTaskId() );
        commentDto.setTimestamp( comment.getTimestamp() );
        commentDto.setUserId( comment.getUserId() );
        commentDto.setText( comment.getText() );

        return commentDto;
    }

    @Override
    public List<CommentDto> commentListToDtoList(List<Comment> comments) {
        if ( comments == null ) {
            return null;
        }

        List<CommentDto> list = new ArrayList<CommentDto>( comments.size() );
        for ( Comment comment : comments ) {
            list.add( commentToDto( comment ) );
        }

        return list;
    }

    @Override
    public TaskManagementDto modelToDto(TaskManagement model) {
        if ( model == null ) {
            return null;
        }

        TaskManagementDto taskManagementDto = new TaskManagementDto();

        taskManagementDto.setId( model.getId() );
        taskManagementDto.setReferenceId( model.getReferenceId() );
        taskManagementDto.setReferenceType( model.getReferenceType() );
        taskManagementDto.setTask( model.getTask() );
        taskManagementDto.setDescription( model.getDescription() );
        taskManagementDto.setStatus( model.getStatus() );
        taskManagementDto.setAssigneeId( model.getAssigneeId() );
        taskManagementDto.setTaskDeadlineTime( model.getTaskDeadlineTime() );
        taskManagementDto.setPriority( model.getPriority() );

        return taskManagementDto;
    }

    @Override
    public TaskManagement dtoToModel(TaskManagementDto dto) {
        if ( dto == null ) {
            return null;
        }

        TaskManagement taskManagement = new TaskManagement();

        taskManagement.setId( dto.getId() );
        taskManagement.setReferenceId( dto.getReferenceId() );
        taskManagement.setReferenceType( dto.getReferenceType() );
        taskManagement.setTask( dto.getTask() );
        taskManagement.setDescription( dto.getDescription() );
        taskManagement.setStatus( dto.getStatus() );
        taskManagement.setAssigneeId( dto.getAssigneeId() );
        taskManagement.setTaskDeadlineTime( dto.getTaskDeadlineTime() );
        taskManagement.setPriority( dto.getPriority() );

        return taskManagement;
    }

    @Override
    public List<TaskManagementDto> modelListToDtoList(List<TaskManagement> models) {
        if ( models == null ) {
            return null;
        }

        List<TaskManagementDto> list = new ArrayList<TaskManagementDto>( models.size() );
        for ( TaskManagement taskManagement : models ) {
            list.add( modelToDto( taskManagement ) );
        }

        return list;
    }
}
