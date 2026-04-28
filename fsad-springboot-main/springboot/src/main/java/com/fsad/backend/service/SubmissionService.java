package com.fsad.backend.service;

import com.fsad.backend.model.*;
import com.fsad.backend.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public SubmissionService(SubmissionRepository submissionRepository,
                             AssignmentRepository assignmentRepository) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public List<Submission> getByAssignment(Long assignmentId, User teacher) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found."));
        if (!assignment.getTeacher().getId().equals(teacher.getId()))
            throw new RuntimeException("Unauthorized.");
        return submissionRepository.findByAssignmentOrderByCreatedAtDesc(assignment);
    }

    public List<Submission> getMySubmissions(User student) {
        return submissionRepository.findByStudentOrderByCreatedAtDesc(student);
    }

    public Submission submit(Long assignmentId, String comment, MultipartFile file, User student) throws IOException {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found."));

        if (submissionRepository.existsByAssignmentAndStudent(assignment, student))
            throw new RuntimeException("You have already submitted this assignment.");

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        LocalDate dueDate = LocalDate.parse(assignment.getDueDate());
        boolean isLate = LocalDate.now().isAfter(dueDate);

        String submittedAt = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Submission submission = Submission.builder()
                .assignment(assignment)
                .student(student)
                .studentName(student.getName())
                .rollNo(student.getRollNo())
                .comment(comment != null ? comment : "")
                .fileName(file.getOriginalFilename())
                .filePath(fileName)
                .submittedAt(submittedAt)
                .status(isLate ? Submission.Status.LATE : Submission.Status.PENDING)
                .build();

        return submissionRepository.save(submission);
    }

    public Submission grade(Long id, Map<String, Object> body, User teacher) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found."));

        if (!submission.getAssignment().getTeacher().getId().equals(teacher.getId()))
            throw new RuntimeException("Unauthorized.");

        int grade = Integer.parseInt(body.get("grade").toString());
        if (grade < 0 || grade > submission.getAssignment().getTotalMarks())
            throw new RuntimeException("Grade out of range.");

        submission.setGrade(grade);
        submission.setFeedback(body.getOrDefault("feedback", "").toString());
        submission.setStatus(Submission.Status.GRADED);
        return submissionRepository.save(submission);
    }

    public Submission getById(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found."));
    }
}
