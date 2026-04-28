package com.fsad.backend.service;

import com.fsad.backend.model.*;
import com.fsad.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatsService {

    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository       userRepository;

    public StatsService(AssignmentRepository assignmentRepository,
                        SubmissionRepository submissionRepository,
                        UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository       = userRepository;
    }

    public Map<String, Object> teacherStats(User teacher) {
        List<Assignment> assignments = assignmentRepository.findByTeacherOrderByCreatedAtDesc(teacher);
        List<Submission> submissions = assignments.isEmpty()
                ? Collections.emptyList()
                : submissionRepository.findByAssignmentIn(assignments);

        long graded  = submissions.stream().filter(s -> s.getStatus() == Submission.Status.GRADED).count();
        long pending = submissions.stream().filter(s -> s.getStatus() == Submission.Status.PENDING).count();
        long late    = submissions.stream().filter(s -> s.getStatus() == Submission.Status.LATE).count();
        long active  = assignments.stream().filter(a -> a.getStatus() == Assignment.Status.ACTIVE).count();

        double avgGrade = submissions.stream()
                .filter(s -> s.getStatus() == Submission.Status.GRADED && s.getGrade() != null)
                .mapToInt(Submission::getGrade)
                .average().orElse(0);

        long totalStudents = userRepository.countByRole(User.Role.STUDENT);

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalAssignments",   assignments.size());
        stats.put("activeAssignments",  active);
        stats.put("totalSubmissions",   submissions.size());
        stats.put("pendingSubmissions", pending);
        stats.put("gradedSubmissions",  graded);
        stats.put("lateSubmissions",    late);
        stats.put("avgGrade",           Math.round(avgGrade));
        stats.put("totalStudents",      totalStudents);
        return stats;
    }

    public Map<String, Object> studentStats(User student) {
        long totalAssignments = assignmentRepository.findByStatusInOrderByCreatedAtDesc(
                Arrays.asList(Assignment.Status.ACTIVE, Assignment.Status.GRADED)).size();

        List<Submission> mySubmissions = submissionRepository.findByStudentOrderByCreatedAtDesc(student);

        long submitted = mySubmissions.size();
        long graded    = mySubmissions.stream().filter(s -> s.getStatus() == Submission.Status.GRADED).count();
        long late      = mySubmissions.stream().filter(s -> s.getStatus() == Submission.Status.LATE).count();
        long pending   = totalAssignments - submitted;

        double avgScore = mySubmissions.stream()
                .filter(s -> s.getStatus() == Submission.Status.GRADED && s.getGrade() != null)
                .mapToDouble(s -> (s.getGrade() * 100.0) / s.getAssignment().getTotalMarks())
                .average().orElse(0);

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalAssignments", totalAssignments);
        stats.put("submitted",  submitted);
        stats.put("pending",    pending);
        stats.put("graded",     graded);
        stats.put("late",       late);
        stats.put("avgScore",   Math.round(avgScore));
        return stats;
    }
}
