package com.zeed.assignment.sms.security;



import com.zeed.assignment.sms.models.AdminUser;
import com.zeed.assignment.sms.models.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsTokenEnvelope implements UserDetails{

    public Student student;

    public AdminUser adminUser;

    public UserDetailsTokenEnvelope( Student student, AdminUser adminUser) {
        this.student = student;
        this.adminUser = adminUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return (student != null) ? student.getRegistrationInfo().getRegistrationNo() : adminUser.getUsername() ;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
