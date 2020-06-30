package com.example.demo.model;

import lombok.Data;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 1, max = 100, message = "Title  must be between 1 and 255 characters long")
    @Column(name = "title")
    private String title;

    @Size(min = 1, max = 100, message = "Description  must be between 1 and 255 characters long")
    @Column(name="description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "created_by")
    private String createdBy;


    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "assigned_to")
    private String assignedTo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' +
                ", status=" + status + ", createdBy='" + createdBy + '\'' + ", createdAt=" + createdAt +
                ", assignedTo='" + assignedTo + '\'' + '}';
    }
}
