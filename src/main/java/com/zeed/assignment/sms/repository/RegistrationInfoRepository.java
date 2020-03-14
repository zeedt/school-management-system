package com.zeed.assignment.sms.repository;

import com.zeed.assignment.sms.models.RegistrationInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationInfoRepository extends JpaRepository<RegistrationInfo,Long> {

    RegistrationInfo findOneByRegistrationNo(String registrationNumber);

    Page<RegistrationInfo> findAllByIdNotNull(Pageable pageable);

}
