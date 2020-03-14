package com.zeed.assignment.sms.repository;

import com.zeed.assignment.sms.models.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    AdminUser findOneByUsernameAndPassword(String username, String password);

}
