package com.zeed.assignment.sms.security;

import com.zeed.assignment.sms.models.Student;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationProvider<T extends Student> {

    Authentication authenticate(Authentication var1) throws AuthenticationException;


}
