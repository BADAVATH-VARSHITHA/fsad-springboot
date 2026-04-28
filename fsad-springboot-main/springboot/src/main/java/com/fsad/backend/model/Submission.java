package com.fsad.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions",
       uniqueConstraints = @UniqueConstraint(columnNames = {"assignment_id", "student_id"}))
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignment_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties({"password", "hibernateLazyInitializer", "handler"})
    private User student;

    private String studentName;
    private String rollNo;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private String fileName;
    private String filePath;
    private String submittedAt;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private Integer grade;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() { createdAt = LocalDateTime.now(); }

    public enum Status { PENDING, GRADED, LATE }

    // ── Getters ───────────────────────────────────────────────────────────────
    public Long getId()             { return id; }
    public Assignment getAssignment() { return assignment; }
    public User getStudent()        { return student; }
    public String getStudentName()  { return studentName; }
    public String getRollNo()       { return rollNo; }
    public String getComment()      { return comment; }
    public String getFileName()     { return fileName; }
    public String getFilePath()     { return filePath; }
    public String getSubmittedAt()  { return submittedAt; }
    public Status getStatus()       { return status; }
    public Integer getGrade()       { return grade; }
    public String getFeedback()     { return feedback; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setId(Long id)                   { this.id = id; }
    public void setAssignment(Assignment a)      { this.assignment = a; }
    public void setStudent(User student)         { this.student = student; }
    public void setStudentName(String n)         { this.studentName = n; }
    public void setRollNo(String r)              { this.rollNo = r; }
    public void setComment(String c)             { this.comment = c; }
    public void setFileName(String f)            { this.fileName = f; }
    public void setFilePath(String f)            { this.filePath = f; }
    public void setSubmittedAt(String s)         { this.submittedAt = s; }
    public void setStatus(Status s)              { this.status = s; }
    public void setGrade(Integer g)              { this.grade = g; }
    public void setFeedback(String f)            { this.feedback = f; }
    public void setCreatedAt(LocalDateTime c)    { this.createdAt = c; }

    // ── Builder ───────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Submission s = new Submission();
        public Builder assignment(Assignment a)  { s.assignment = a; return this; }
        public Builder student(User u)           { s.student = u; return this; }
        public Builder studentName(String n)     { s.studentName = n; return this; }
        public Builder rollNo(String r)          { s.rollNo = r; return this; }
        public Builder comment(String c)         { s.comment = c; return this; }
        public Builder fileName(String f)        { s.fileName = f; return this; }
        public Builder filePath(String f)        { s.filePath = f; return this; }
        public Builder submittedAt(String t)     { s.submittedAt = t; return this; }
        public Builder status(Status st)         { s.status = st; return this; }
        public Builder grade(Integer g)          { s.grade = g; return this; }
        public Builder feedback(String f)        { s.feedback = f; return this; }
        public Submission build()                { return s; }
    }
}
