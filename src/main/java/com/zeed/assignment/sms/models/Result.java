package com.zeed.assignment.sms.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(
        uniqueConstraints = { @UniqueConstraint(name = "UQ_Subject_Student", columnNames = {"subject_id","student_registration_number"}) }
)
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @NotNull
    private Subject subject;

    @NotNull
    @Column(name = "student_registration_number")
    private String studentRegistrationNumber;

    @NotNull
    private Integer score;

    @NotNull
    private Date UploadedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }


    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getStudentRegistrationNumber() {
        return studentRegistrationNumber;
    }

    public void setStudentRegistrationNumber(String studentRegistrationNumber) {
        this.studentRegistrationNumber = studentRegistrationNumber;
    }

    public Date getUploadedDate() {
        return UploadedDate;
    }

    public void setUploadedDate(Date uploadedDate) {
        UploadedDate = uploadedDate;
    }
}
