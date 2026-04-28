package com.fsad.backend.dto;

public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private String department;
    private String rollNo;
    private String branch;
    private String semester;

    public String getName()       { return name; }
    public String getEmail()      { return email; }
    public String getPassword()   { return password; }
    public String getDepartment() { return department; }
    public String getRollNo()     { return rollNo; }
    public String getBranch()     { return branch; }
    public String getSemester()   { return semester; }

    public void setName(String n)        { this.name = n; }
    public void setEmail(String e)       { this.email = e; }
    public void setPassword(String p)    { this.password = p; }
    public void setDepartment(String d)  { this.department = d; }
    public void setRollNo(String r)      { this.rollNo = r; }
    public void setBranch(String b)      { this.branch = b; }
    public void setSemester(String s)    { this.semester = s; }
}
