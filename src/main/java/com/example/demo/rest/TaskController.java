package com.example.demo.rest;

import com.example.demo.dto.TaskDto;
import com.example.demo.model.Status;
import com.example.demo.model.Task;
import com.example.demo.service.TaskService;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity create(@Valid @RequestBody Task task, Principal principal) {
        Task createdTask = taskService.create(task, principal);
        return ResponseEntity.ok(TaskDto.toTaskDto(createdTask));
    }

    @PostMapping("/update/{taskId}")
    public ResponseEntity update(@PathVariable long taskId, @RequestBody String description) {
        Task updatedTask = taskService.update(description, taskId);
        if (updatedTask == null) {
            return new ResponseEntity("Could not find task with id " + taskId, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(TaskDto.toTaskDto(updatedTask));
    }


    @PostMapping("/changestatus/{taskId}/{status}")
    public ResponseEntity changeStatus(@PathVariable long taskId, @PathVariable String status) {
        Status taskStatus;
        try {
            taskStatus = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity("Status is invalid", HttpStatus.BAD_REQUEST);
        }
        Task updatedTask = taskService.changeStatus(taskStatus, taskId);
        if (updatedTask == null) {
            return new ResponseEntity("Task with id " + taskId + " does not exist", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(TaskDto.toTaskDto(updatedTask));
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity delete(@PathVariable long taskId) {
        Task updatedTask = taskService.delete(taskId);
        if (updatedTask == null) {
            return new ResponseEntity("Could not find task with id " + taskId, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(TaskDto.toTaskDto(updatedTask));
    }

    @GetMapping("/filter/{status}")
    public ResponseEntity filterByStatus(@PathVariable String status) {
        Status taskStatus;
        try {
            taskStatus = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity("Status is invalid", HttpStatus.BAD_REQUEST);
        }
        List<TaskDto> tasks = taskService.filterByStatus(taskStatus)
                .stream()
                .map(TaskDto::toTaskDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/assign/{taskId}/{userId}")
    public ResponseEntity changerAssignedTo(@PathVariable long taskId, @PathVariable long userId) {
        Task task = taskService.changerTaskAssignedTo(taskId, userId);
        if (task == null) {
            return new ResponseEntity("Task id or user id is invalid", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(TaskDto.toTaskDto(task));
    }
}

