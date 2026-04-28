package com.fsad.backend.repository;

import com.fsad.backend.model.Assignment;
import com.fsad.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByTeacherOrderByCreatedAtDesc(User teacher);
    List<Assignment> findByStatusInOrderByCreatedAtDesc(List<Assignment.Status> statuses);
    long countByTeacher(User teacher);
    long countByTeacherAndStatus(User teacher, Assignment.Status status);
}
