package com.zeed.assignment.sms.security.providers;

import com.zeed.assignment.sms.models.AdminUser;
import com.zeed.assignment.sms.models.Authority;
import com.zeed.assignment.sms.models.Student;
import com.zeed.assignment.sms.repository.AdminUserRepository;
import com.zeed.assignment.sms.repository.StudentRepository;
import com.zeed.assignment.sms.security.AuthenticationProvider;
import com.zeed.assignment.sms.security.UserDetailsTokenEnvelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Student managedUser = studentRepository.findByRegistrationInfo_RegistrationNoAndPin(authentication.getPrincipal().toString(), authentication.getCredentials().toString());


        if (managedUser == null ) {
            AdminUser adminUser = adminUserRepository.findOneByUsernameAndPassword(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
            if (adminUser == null) {
                throw new BadCredentialsException("Invalid credentials");
            }
            UserDetailsTokenEnvelope userDetailsTokenEnvelope = new UserDetailsTokenEnvelope(null, adminUser);
            Authority authority = new Authority();
            authority.setAuthority("ADMIN");
            UsernamePasswordAuthenticationToken authenticationDetails = new UsernamePasswordAuthenticationToken(userDetailsTokenEnvelope,null, Collections.singleton(authority));
            return authenticationDetails;
        }else{
            UserDetailsTokenEnvelope userDetailsTokenEnvelope = new UserDetailsTokenEnvelope(managedUser, null);
            Authority authority = new Authority();
            authority.setAuthority("STUDENT");
            UsernamePasswordAuthenticationToken authenticationDetails = new UsernamePasswordAuthenticationToken(userDetailsTokenEnvelope,null, Collections.singleton(authority));
                return authenticationDetails;
        }

    }

}
