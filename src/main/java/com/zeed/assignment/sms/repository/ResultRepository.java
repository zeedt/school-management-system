package com.zeed.assignment.sms.repository;

import com.zeed.assignment.sms.enums.Class;
import com.zeed.assignment.sms.models.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    Result findTopBySubject_SubjectCodeAndStudentRegistrationNumber(String subjectCode, String regNo);

    Page<Result> findAllBySubject_AClassAndSubject_SubjectCode(Class aclass, String subjectCode, Pageable pageable);

    List<Result> findAllByStudentRegistrationNumber(String registrationNumber);

}
