package com.railse.hiring.workforcemgmt.controller;


import com.railse.hiring.workforcemgmt.common.model.response.Response;
import com.railse.hiring.workforcemgmt.common.model.enums.Priority;
import com.railse.hiring.workforcemgmt.dto.TaskManagementDto;
import com.railse.hiring.workforcemgmt.dto.TaskCreateRequest;
import com.railse.hiring.workforcemgmt.dto.UpdateTaskRequest;
import com.railse.hiring.workforcemgmt.dto.AssignByReferenceRequest;
import com.railse.hiring.workforcemgmt.dto.TaskFetchByDateRequest;
import com.railse.hiring.workforcemgmt.dto.AddCommentRequest;
import com.railse.hiring.workforcemgmt.dto.UpdateTaskPriorityRequest;
import com.railse.hiring.workforcemgmt.service.TaskManagementService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController

@RequestMapping("/task-mgmt")

public class TaskManagementController {


    private final TaskManagementService taskManagementService;


    public TaskManagementController(TaskManagementService taskManagementService) {

        this.taskManagementService = taskManagementService;

    }


    @GetMapping("/{id}")

    public Response<TaskManagementDto> getTaskById(@PathVariable Long id) {

        return new Response<>(taskManagementService.findTaskById(id));

    }


    @PostMapping("/create")

    public Response<List<TaskManagementDto>> createTasks(@RequestBody TaskCreateRequest request) {

        return new Response<>(taskManagementService.createTasks(request));

    }


    @PostMapping("/update")

    public Response<List<TaskManagementDto>> updateTasks(@RequestBody UpdateTaskRequest request) {

        return new Response<>(taskManagementService.updateTasks(request));

    }


    @PostMapping("/assign-by-ref")

    public Response<String> assignByReference(@RequestBody AssignByReferenceRequest request) {

        return new Response<>(taskManagementService.assignByReference(request));

    }


    @PostMapping("/fetch-by-date/v2")

    public Response<List<TaskManagementDto>> fetchByDate(@RequestBody TaskFetchByDateRequest request) {

        return new Response<>(taskManagementService.fetchTasksByDate(request));

    }


    @PostMapping("/tasks/{taskId}/comments")
    public Response<TaskManagementDto> addCommentToTask(@PathVariable Long taskId, @Valid @RequestBody AddCommentRequest request) {
        return new Response<>(taskManagementService.addCommentToTask(taskId, request.getUserId(), request.getCommentText()));
    }


    @PutMapping("/tasks/{taskId}/priority")
    public Response<TaskManagementDto> updateTaskPriority(@PathVariable Long taskId,
               @Valid @RequestBody UpdateTaskPriorityRequest request) {
        String userId = "manager_user";
        return new Response<>(taskManagementService.updateTaskPriority(taskId, request.getNewPriority(), userId));
    }

    @GetMapping("/tasks/priority/{priority}")
    public Response<List<TaskManagementDto>> getTasksByPriority(@PathVariable Priority priority) {
        return new Response<>(taskManagementService.getTasksByPriority(priority));
    }

}

