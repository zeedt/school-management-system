package com.zeed.assignment.sms.repository;

import com.zeed.assignment.sms.enums.Class;
import com.zeed.assignment.sms.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Subject findTopByAClassAndSubjectCode(Class aclass, String subjectCode);

}
