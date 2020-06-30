package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class TaskTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerApplication.class, args);
    }
}

// TODO: 29.06.2020
//  echo "# Task-Tracker-back-end-only-" >> README.md
//git init
//git add README.md
//git commit -m "first commit"
//git remote add origin https://github.com/Mykhaylo12/Task-Tracker-back-end-only-.git
//git push -u origin master

//git remote add origin https://github.com/Mykhaylo12/Task-Tracker-back-end-only-.git
//git push -u origin master