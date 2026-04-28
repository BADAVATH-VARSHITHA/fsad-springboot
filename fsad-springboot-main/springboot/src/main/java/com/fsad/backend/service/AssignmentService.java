package com.fsad.backend.service;

import com.fsad.backend.model.*;
import com.fsad.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             SubmissionRepository submissionRepository) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
    }

    public List<Assignment> getAll(User user) {
        if (user != null && user.getRole() == User.Role.TEACHER)
            return assignmentRepository.findByTeacherOrderByCreatedAtDesc(user);
        return assignmentRepository.findByStatusInOrderByCreatedAtDesc(
                Arrays.asList(Assignment.Status.ACTIVE, Assignment.Status.GRADED));
    }

    public Assignment getById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found."));
    }

    public Assignment create(Map<String, Object> body, User teacher) {
        Assignment assignment = Assignment.builder()
                .title((String) body.get("title"))
                .description((String) body.get("description"))
                .dueDate((String) body.get("dueDate"))
                .subject((String) body.get("subject"))
                .totalMarks(Integer.parseInt(body.get("totalMarks").toString()))
                .status(Assignment.Status.ACTIVE)
                .teacher(teacher)
                .build();
        return assignmentRepository.save(assignment);
    }

    public Assignment update(Long id, Map<String, Object> body, User teacher) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found."));
        if (!assignment.getTeacher().getId().equals(teacher.getId()))
            throw new RuntimeException("Unauthorized.");

        if (body.containsKey("title"))       assignment.setTitle((String) body.get("title"));
        if (body.containsKey("description")) assignment.setDescription((String) body.get("description"));
        if (body.containsKey("dueDate"))     assignment.setDueDate((String) body.get("dueDate"));
        if (body.containsKey("subject"))     assignment.setSubject((String) body.get("subject"));
        if (body.containsKey("totalMarks"))  assignment.setTotalMarks(Integer.parseInt(body.get("totalMarks").toString()));
        if (body.containsKey("status"))      assignment.setStatus(Assignment.Status.valueOf(((String) body.get("status")).toUpperCase()));

        return assignmentRepository.save(assignment);
    }

    public void delete(Long id, User teacher) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found."));
        if (!assignment.getTeacher().getId().equals(teacher.getId()))
            throw new RuntimeException("Unauthorized.");
        submissionRepository.deleteAll(submissionRepository.findByAssignmentOrderByCreatedAtDesc(assignment));
        assignmentRepository.delete(assignment);
    }

    public List<Map<String, Object>> getMyAssignments(User student) {
        List<Assignment> assignments = assignmentRepository.findByStatusInOrderByCreatedAtDesc(
                Arrays.asList(Assignment.Status.ACTIVE, Assignment.Status.GRADED));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Assignment a : assignments) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", a.getId());
            map.put("title", a.getTitle());
            map.put("description", a.getDescription());
            map.put("dueDate", a.getDueDate());
            map.put("subject", a.getSubject());
            map.put("totalMarks", a.getTotalMarks());
            map.put("status", a.getStatus().name().toLowerCase());
            map.put("createdAt", a.getCreatedAt());
            Optional<Submission> sub = submissionRepository.findByAssignmentAndStudent(a, student);
            map.put("submissionStatus", sub.map(s -> s.getStatus().name().toLowerCase()).orElse(null));
            map.put("submissionId", sub.map(Submission::getId).orElse(null));
            result.add(map);
        }
        return result;
    }
}
