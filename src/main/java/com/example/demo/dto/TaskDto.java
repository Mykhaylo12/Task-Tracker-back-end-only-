package com.example.demo.dto;

import com.example.demo.model.Status;
import com.example.demo.model.Task;
import lombok.Data;
import java.util.Date;

@Data
public class TaskDto {
    private long id;
    private String title;
    private String description;
    private Status status;
    private String createdBy;
    private Date createAt;
    private String assignedTo;

    public static TaskDto toTaskDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setStatus(task.getStatus());
        taskDto.setCreatedBy(task.getCreatedBy());
        taskDto.setAssignedTo(task.getAssignedTo());
        taskDto.setCreateAt(task.getCreatedAt());
        return taskDto;
    }
}
