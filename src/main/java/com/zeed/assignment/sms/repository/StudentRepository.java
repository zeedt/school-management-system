package com.zeed.assignment.sms.repository;

import com.zeed.assignment.sms.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByRegistrationInfo_RegistrationNoAndPin(String regNo, String pin);

    Student findFirstByRegistrationInfo_RegistrationNo(String regNo);

    Student findByEmail(String email);

    Page<Student> findAllByIdNotNull(Pageable pageable);
}
