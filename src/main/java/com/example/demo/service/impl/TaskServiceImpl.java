package com.example.demo.service.impl;

import com.example.demo.model.Status;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Task create(Task task, Principal principal) {
        String createdBy = principal.getName();
        task.setCreatedAt(new Date());
        if (task.getAssignedTo() == null) {
            task.setAssignedTo(createdBy);
        }
        task.setStatus(Status.NEW);
        task.setCreatedBy(createdBy);
        task.setUser(userRepository.findByEmail(createdBy));
        log.info("Task with id {} created",task.getId());
        return taskRepository.save(task);
    }

    @Override
    public Task update(String description, long taskId) {
        taskRepository.update(description, taskId);
        return taskRepository.findById(taskId).orElse(null);
    }

    @Override
    public Task changeStatus(Status status, long taskId) {
        taskRepository.changeStatus(status, taskId);
        return taskRepository.findById(taskId).orElse(null);
    }

    @Override
    public Task delete(long taskId) {
        Task taskToDelete = taskRepository.findById(taskId).orElse(null);
        if (taskToDelete == null) {
            return null;
        }
        taskRepository.deleteById(taskId);
        return taskToDelete;
    }

    @Override
    public List<Task> filterByStatus(Status status) {
        return taskRepository.filterByStatus(status);
    }

    @Override
    public Task changerTaskAssignedTo(long taskId, long userId) {
        Task taskToUpdate = taskRepository.findById(taskId).orElse(null);
        User userAssignTo = userRepository.findById(userId).orElse(null);
        if (taskToUpdate == null || userAssignTo == null) {
            return null;
        }
        taskRepository.changerTaskAssignedTo(taskId, userAssignTo, userAssignTo.getEmail());
        return taskRepository.findById(taskId).orElse(null);
    }
}
