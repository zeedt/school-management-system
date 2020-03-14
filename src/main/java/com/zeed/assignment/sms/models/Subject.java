package com.zeed.assignment.sms.models;

import com.zeed.assignment.sms.enums.Class;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints = { @UniqueConstraint(name = "UQ_Subject_Student", columnNames = {"subject_code","a_class"}) }
)
public class Subject {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "subject_code")
    private String subjectCode;

    private String subjectDescription;

    @Column(name = "a_class")
    @Enumerated(value = EnumType.STRING)
    private Class aClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectDescription() {
        return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
        this.subjectDescription = subjectDescription;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }
}
