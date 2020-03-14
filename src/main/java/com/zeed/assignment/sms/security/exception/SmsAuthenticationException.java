package com.zeed.assignment.sms.security.exception;

import org.springframework.security.core.AuthenticationException;

public class SmsAuthenticationException extends AuthenticationException {
    public SmsAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public SmsAuthenticationException(String msg) {
        super(msg);
    }
}
