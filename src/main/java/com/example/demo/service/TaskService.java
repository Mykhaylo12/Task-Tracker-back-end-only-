package com.example.demo.service;

import com.example.demo.model.Status;
import com.example.demo.model.Task;
import java.security.Principal;
import java.util.List;

public interface TaskService {
    Task create(Task task, Principal principal);

    Task update(String description, long taskId);

    Task changeStatus(Status status, long taskId);

    Task delete(long taskId);

    List<Task> filterByStatus(Status status);

    Task changerTaskAssignedTo(long taskId, long userId);
}
