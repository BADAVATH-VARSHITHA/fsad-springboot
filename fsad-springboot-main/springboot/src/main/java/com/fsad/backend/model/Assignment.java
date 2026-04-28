package com.fsad.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String dueDate;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private Integer totalMarks;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", nullable = false)
    @JsonIgnoreProperties({"password", "hibernateLazyInitializer", "handler"})
    private User teacher;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() { createdAt = LocalDateTime.now(); }

    public enum Status { ACTIVE, GRADED, CLOSED }

    // ── Getters ───────────────────────────────────────────────────────────────
    public Long getId()              { return id; }
    public String getTitle()         { return title; }
    public String getDescription()   { return description; }
    public String getDueDate()       { return dueDate; }
    public String getSubject()       { return subject; }
    public Integer getTotalMarks()   { return totalMarks; }
    public Status getStatus()        { return status; }
    public User getTeacher()         { return teacher; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setId(Long id)              { this.id = id; }
    public void setTitle(String t)          { this.title = t; }
    public void setDescription(String d)    { this.description = d; }
    public void setDueDate(String d)        { this.dueDate = d; }
    public void setSubject(String s)        { this.subject = s; }
    public void setTotalMarks(Integer m)    { this.totalMarks = m; }
    public void setStatus(Status s)         { this.status = s; }
    public void setTeacher(User t)          { this.teacher = t; }
    public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }

    // ── Builder ───────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Assignment a = new Assignment();
        public Builder title(String t)       { a.title = t; return this; }
        public Builder description(String d) { a.description = d; return this; }
        public Builder dueDate(String d)     { a.dueDate = d; return this; }
        public Builder subject(String s)     { a.subject = s; return this; }
        public Builder totalMarks(Integer m) { a.totalMarks = m; return this; }
        public Builder status(Status s)      { a.status = s; return this; }
        public Builder teacher(User t)       { a.teacher = t; return this; }
        public Assignment build()            { return a; }
    }
}
