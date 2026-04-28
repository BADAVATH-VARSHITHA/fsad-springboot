package com.fsad.backend.repository;

import com.fsad.backend.model.Assignment;
import com.fsad.backend.model.Submission;
import com.fsad.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignmentOrderByCreatedAtDesc(Assignment assignment);
    List<Submission> findByStudentOrderByCreatedAtDesc(User student);
    Optional<Submission> findByAssignmentAndStudent(Assignment assignment, User student);
    boolean existsByAssignmentAndStudent(Assignment assignment, User student);
    long countByAssignmentIn(List<Assignment> assignments);
    long countByAssignmentInAndStatus(List<Assignment> assignments, Submission.Status status);
    List<Submission> findByAssignmentIn(List<Assignment> assignments);
    long countByStudent(User student);
    long countByStudentAndStatus(User student, Submission.Status status);
}
