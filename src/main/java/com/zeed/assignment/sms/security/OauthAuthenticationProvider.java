package com.zeed.assignment.sms.security;

import com.zeed.assignment.sms.models.Student;
import com.zeed.assignment.sms.repository.StudentRepository;
import com.zeed.assignment.sms.security.exception.SmsAuthenticationException;
import com.zeed.assignment.sms.security.providers.UserAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OauthAuthenticationProvider implements AuthenticationProvider {

    //For now, it's just one authentication provider that is needed
    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = (authentication.getCredentials() !=null ) ? authentication.getCredentials().toString() : null;

        UsernamePasswordAuthenticationToken tokenAuthentication = new UsernamePasswordAuthenticationToken(userName, password, null);
        tokenAuthentication.setDetails(authentication.getDetails());

        List<com.zeed.assignment.sms.security.AuthenticationProvider> authenticationProviders = new ArrayList<>();
        authenticationProviders.add(userAuthenticationProvider);

            authentication = userAuthenticationProvider.authenticate(tokenAuthentication);
            if (authentication!=null) {
                return authentication;
            }

        return null;
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}
