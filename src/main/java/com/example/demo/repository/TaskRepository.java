package com.example.demo.repository;

import com.example.demo.model.Status;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Task t set t.status=:status where t.id =:taskId")
    void changeStatus(@Param("status") Status status, @Param("taskId") long taskId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Task t set t.description=:description where t.id =:taskId")
    void update(@Param("description") String description, @Param("taskId") long taskId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("select t from Task t where t.status =:status order by t.createdAt")
    List<Task> filterByStatus(@Param("status") Status status);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Task t set t.user=:user , t.assignedTo=:emailAssignedTo  where t.id =:taskId")
    void changerTaskAssignedTo(@Param("taskId") long taskId, @Param("user") User user,
                               @Param("emailAssignedTo") String emailAssignedTo);
}
