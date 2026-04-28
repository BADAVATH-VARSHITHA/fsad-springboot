package com.fsad.backend.dto;

import com.fsad.backend.model.User;

public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String department;
    private String joined;
    private String rollNo;
    private String branch;
    private String semester;
    private String avatar;

    // ── Getters ───────────────────────────────────────────────────────────────
    public Long getId()          { return id; }
    public String getName()      { return name; }
    public String getEmail()     { return email; }
    public String getRole()      { return role; }
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
    public void setRole(String r)        { this.role = r; }
    public void setDepartment(String d)  { this.department = d; }
    public void setJoined(String j)      { this.joined = j; }
    public void setRollNo(String r)      { this.rollNo = r; }
    public void setBranch(String b)      { this.branch = b; }
    public void setSemester(String s)    { this.semester = s; }
    public void setAvatar(String a)      { this.avatar = a; }

    // ── Static factory ────────────────────────────────────────────────────────
    public static UserDTO fromUser(User user) {
        UserDTO dto = new UserDTO();
        dto.id         = user.getId();
        dto.name       = user.getName();
        dto.email      = user.getEmail();
        dto.role       = user.getRole().name().toLowerCase();
        dto.department = user.getDepartment();
        dto.joined     = user.getJoined();
        dto.rollNo     = user.getRollNo();
        dto.branch     = user.getBranch();
        dto.semester   = user.getSemester();
        dto.avatar     = user.getAvatar();
        return dto;
    }

    // ── Builder ───────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final UserDTO dto = new UserDTO();
        public Builder id(Long id)           { dto.id = id; return this; }
        public Builder name(String n)        { dto.name = n; return this; }
        public Builder email(String e)       { dto.email = e; return this; }
        public Builder role(String r)        { dto.role = r; return this; }
        public Builder department(String d)  { dto.department = d; return this; }
        public Builder joined(String j)      { dto.joined = j; return this; }
        public Builder rollNo(String r)      { dto.rollNo = r; return this; }
        public Builder branch(String b)      { dto.branch = b; return this; }
        public Builder semester(String s)    { dto.semester = s; return this; }
        public Builder avatar(String a)      { dto.avatar = a; return this; }
        public UserDTO build()               { return dto; }
    }
}
