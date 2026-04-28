package com.fsad.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String department;
    private String joined;
    private String rollNo;
    private String branch;
    private String semester;
    private String avatar;

    public enum Role { TEACHER, STUDENT }

    // ── Getters ───────────────────────────────────────────────────────────────
    public Long getId()          { return id; }
    public String getName()      { return name; }
    public String getEmail()     { return email; }
    public String getPassword()  { return password; }
    public Role getRole()        { return role; }
    public String getDepartment() { return department; }
    public String getJoined()    { return joined; }
    public String getRollNo()    { return rollNo; }
    public String getBranch()    { return branch; }
    public String getSemester()  { return semester; }
    public String getAvatar()    { return avatar; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setId(Long id)           { this.id = id; }
    public void setName(String n)        { this.name = n; }
    public void setEmail(String e)       { this.email = e; }
    public void setPassword(String p)    { this.password = p; }
    public void setRole(Role r)          { this.role = r; }
    public void setDepartment(String d)  { this.department = d; }
    public void setJoined(String j)      { this.joined = j; }
    public void setRollNo(String r)      { this.rollNo = r; }
    public void setBranch(String b)      { this.branch = b; }
    public void setSemester(String s)    { this.semester = s; }
    public void setAvatar(String a)      { this.avatar = a; }

    // ── Builder ───────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final User u = new User();
        public Builder name(String n)        { u.name = n; return this; }
        public Builder email(String e)       { u.email = e; return this; }
        public Builder password(String p)    { u.password = p; return this; }
        public Builder role(Role r)          { u.role = r; return this; }
        public Builder department(String d)  { u.department = d; return this; }
        public Builder joined(String j)      { u.joined = j; return this; }
        public Builder rollNo(String r)      { u.rollNo = r; return this; }
        public Builder branch(String b)      { u.branch = b; return this; }
        public Builder semester(String s)    { u.semester = s; return this; }
        public Builder avatar(String a)      { u.avatar = a; return this; }
        public User build()                  { return u; }
    }
}
